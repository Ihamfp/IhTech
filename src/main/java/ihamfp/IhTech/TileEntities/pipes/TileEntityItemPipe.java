package ihamfp.IhTech.TileEntities.pipes;

import java.util.ArrayList;
import java.util.List;

import ihamfp.IhTech.TileEntities.pipes.TileEntityItemPipe.ItemStackHandlerPipe.StackDir;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class TileEntityItemPipe extends TileEntity implements ITickable {
	/***
	 * This ItemStackHandler implementation must _NOT_ be used in GUIs.
	 * Slots here are an abstract thing, it's only a bulk storage of items, in the order of insertion.
	 * Stack amount is variable, absolutely NOT consistent.
	 * Do NOT attempt to use this for anything other than pipes.
	 * 
	 * This handler will _always_ succeed at inserting items.
	 * This handler will _always_ report an available slots (getSlots) amount of (used slots)+1
	 * 
	 * Slot indexes are ignored on insertion.
	 * getStackInSlot and setStackInSlot usages are discouraged.
	 */
	public class ItemStackHandlerPipe implements IItemHandler, IItemHandlerModifiable, INBTSerializable<NBTTagCompound> {
		public class StackDir {
			public ItemStack stack;
			public EnumFacing from;
			public long time;
			
			public StackDir(ItemStack stack) {
				this(stack, null);
			}
			
			public StackDir(ItemStack stack, EnumFacing from) {
				this.stack = stack;
				this.from = from;
				this.time = ItemStackHandlerPipe.this.getTime();
			}
		}
		
		public class dirWrapper implements IItemHandler, IItemHandlerModifiable, INBTSerializable<NBTTagCompound> {
			
			private EnumFacing from;
			private ItemStackHandlerPipe original;
			public dirWrapper(ItemStackHandlerPipe original, EnumFacing dir) {
				this.from = dir;
				this.original = original;
			}

			@Override
			public void setStackInSlot(int slot, ItemStack stack) {
				original.setStackInSlot(slot, stack);
			}

			@Override
			public int getSlots() {
				return original.getSlots();
			}

			@Override
			public ItemStack getStackInSlot(int slot) {
				return original.getStackInSlot(slot);
			}

			@Override
			public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
				original.insertItemInternal(slot, stack, simulate, from);
				return null;
			}

			@Override
			public ItemStack extractItem(int slot, int amount, boolean simulate) {
				return original.extractItem(slot, amount, simulate);
			}

			@Override
			public NBTTagCompound serializeNBT() {
				return original.serializeNBT();
			}

			@Override
			public void deserializeNBT(NBTTagCompound nbt) {
				original.deserializeNBT(nbt);
			}

			@Override
			public int getSlotLimit(int slot) {
				return 64;
			}
		}
		
		public List<StackDir> stacks = new ArrayList<StackDir>(); // almost infinite stack capacity !
		
		@Override
		public NBTTagCompound serializeNBT() {
			NBTTagList nbtList = new NBTTagList();
			for (int i=0;i<stacks.size();i++) {
				NBTTagCompound item = new NBTTagCompound();
				stacks.get(i).stack.writeToNBT(item);
				nbtList.appendTag(item);
			}
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setTag("items", nbtList);
			nbt.setInteger("size", stacks.size());
			return nbt;
		}
		
		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			NBTTagList nbtList = nbt.getTagList("items", Constants.NBT.TAG_COMPOUND);
			stacks.clear();
			for (int i=0;i<nbtList.tagCount();i++) {
				NBTTagCompound tag = nbtList.getCompoundTagAt(i);
				stacks.add(new StackDir(new ItemStack(tag)));
			}
		}

		@Override
		public void setStackInSlot(int slot, ItemStack stack) {
			stacks.set(slot, new StackDir(stack, null));
		}

		@Override
		public int getSlots() {
			return stacks.size();
		}

		@Override
		public ItemStack getStackInSlot(int slot) {
			return stacks.get(slot).stack;
		}

		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
			insertItemInternal(slot, stack, simulate, null);
			
			return null;
		}
		
		public void insertItemInternal(int slot, ItemStack stack, boolean simulate, EnumFacing from) {
			ItemStack copy = stack.copy();
			for (StackDir s : stacks) {
				if (s.stack.isItemEqual(copy) && s.stack.getCount() < copy.getMaxStackSize()) { // yup, we can insert
					ItemStack dest = (simulate)?s.stack.copy():s.stack;
					int finalAmount = dest.getCount() + copy.getCount();
					if (finalAmount < dest.getMaxStackSize()) { // easy case
						dest.setCount(finalAmount);
						copy.setCount(0);
						break;
					} else {
						dest.setCount(dest.getMaxStackSize());;
						copy.setCount(finalAmount - dest.getCount());;
						if (copy.getCount() <= 0) break;
					}
				}
			}
			
			if (!copy.isEmpty() && !simulate) {
				stacks.add(new StackDir(copy, from));
			}
		}

		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			if (slot >= stacks.size() || slot < 0) {
				return null;
			}
			ItemStack s = stacks.get(slot).stack.copy();
			if (!simulate) {
				stacks.remove(slot);
			}
			return s;
		}
		
		public IItemHandler wrap(EnumFacing from) {
			return new dirWrapper(this, from);
		}
		
		public long getTime() {
			return TileEntityItemPipe.this.getWorld().getTotalWorldTime();
		}

		@Override
		public int getSlotLimit(int slot) {
			return 64;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////
	
	private ItemStackHandlerPipe itemStackHandler = new ItemStackHandlerPipe();
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T)(itemStackHandler.wrap(facing));
		}
		return super.getCapability(capability, facing);
	}
	
	protected static int getItemSpeed() { // in ticks/stack
		return 10;
	}
	
	protected void distributeItem(StackDir s) {
		EnumFacing to = null;
		List<EnumFacing> validFaces = new ArrayList<EnumFacing>();
		for (EnumFacing f : EnumFacing.values()) {
			if (this.world.getTileEntity(this.pos.offset(f)) != null) {
				validFaces.add(f);
			}
		}
		
		to = validFaces.get(this.world.rand.nextInt(validFaces.size()));
		
		if (to != null) {
			TileEntity te = this.world.getTileEntity(this.pos.offset(to));
			if (te != null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, to.getOpposite())) {
				IItemHandler teItems = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, to.getOpposite());
				
				for (int i=0;i<teItems.getSlots();i++) {
					s.stack = teItems.insertItem(i, s.stack, false);
				}
			}
		}
		if (s.stack != null && !s.stack.isEmpty()) { // what to do with remaining items ?
			BlockPos dest = this.pos.offset(s.from.getOpposite());
			EntityItem eis = new EntityItem(this.world, dest.getX(), dest.getY(), dest.getZ(), s.stack.copy());
			s.stack = null;
		}
	}
	
	@Override
	public void update() {
		if (this.world.isRemote) return;
		
		long currentTime = this.world.getTotalWorldTime();
		for (StackDir s : this.itemStackHandler.stacks) {
			if (currentTime - s.time > this.getItemSpeed()) { // stack ready to go
				distributeItem(s);
			}
			if (s.stack == null || s.stack.isEmpty()) {
				this.itemStackHandler.stacks.remove(s);
			}
		}
	}
}
