package de.opatut.tradecraft.objects;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerCashRegister extends Container {
	protected TileEntityCashRegister tileEntity;

	public ContainerCashRegister(InventoryPlayer inventoryPlayer,
			TileEntityCashRegister entity) {
		tileEntity = entity;

		addSlotToContainer(new Slot(tileEntity, 0, 80, 27));

		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 9; ++x) {
				addSlotToContainer(new Slot(tileEntity, x + y * 9 + 1,
						8 + 18 * x, 94 + 18 * y));
			}
		}

		bindPlayerInventory(inventoryPlayer);
	}

	private void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 9; ++x) {
				addSlotToContainer(new Slot(inventoryPlayer, x + y * 9 + 9,
						8 + 18 * x, 159 + 18 * y));
			}
		}

		for (int x = 0; x < 9; x++) {
			addSlotToContainer(new Slot(inventoryPlayer, x, 8 + x * 18, 217));
		}

	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tileEntity.isUseableByPlayer(player);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		// TODO: disallow moving other object types to the top

		ItemStack stack = null;
		Slot slotObject = (Slot) inventorySlots.get(slot);

		// null checks and checks if the item can be stacked (maxStackSize > 1)
		if (slotObject != null && slotObject.getHasStack()) {
			ItemStack stackInSlot = slotObject.getStack();
			stack = stackInSlot.copy();

			// merges the item into player inventory since its in the tileEntity
			if (slot < 28) {
				if (!this.mergeItemStack(stackInSlot, 28, 28 + 9 * 4, true)) {
					return null;
				}
			} else {
				if (!this.mergeItemStack(stackInSlot, 0, 28, false)) {
					return null;
				}
			}

			if (stackInSlot.stackSize == 0) {
				slotObject.putStack(null);
			} else {
				slotObject.onSlotChanged();
			}

			if (stackInSlot.stackSize == stack.stackSize) {
				return null;
			}
			slotObject.onPickupFromSlot(player, stackInSlot);
		}
		return stack;
	}
}
