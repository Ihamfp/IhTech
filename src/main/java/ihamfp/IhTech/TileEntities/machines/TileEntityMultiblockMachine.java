package ihamfp.IhTech.TileEntities.machines;

public abstract class TileEntityMultiblockMachine extends TileEntityMachine {
	public boolean isFormed = false;
	
	public void update() {
		if (checkFormed()) {
			isFormed = true;
			super.update();
		} else if (isFormed) { // de-form
			this.clearCookingItems();
			this.processTimeLeft = this.processTime;
		}
	}
	
	protected abstract boolean checkFormed();
}
