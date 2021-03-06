package de.opatut.tradecraft.objects;

import java.util.Random;

import cpw.mods.fml.client.registry.RenderingRegistry;
import de.opatut.tradecraft.Main;
import de.opatut.tradecraft.common.CommonProxy;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCashRegister extends BlockContainer {
	public final static int RENDER_ID = RenderingRegistry
			.getNextAvailableRenderId();

	public BlockCashRegister(int id) {
		super(id, 1, Material.rock);
		setHardness(4.f);
		setResistance(10.f);
		setBlockName("blockCashRegister");
		setTextureFile(CommonProxy.TEXTURE_ICONS);
		setCreativeTab(CreativeTabs.tabMisc);
		float d = 1.f / 16.f;
        setBlockBounds(d, 0.0F, d, 1 - d, 5.f/16.f, 1 - d);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int idk, float what, float these, float are) {
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		
		if (tileEntity == null) return false;

		TileEntityCashRegister cashRegister = (TileEntityCashRegister) tileEntity;
		
		// TODO: select which gui to load here
		if(!world.isRemote) {
			cashRegister.sync();
		}
		
		// either the owner is sneaking, or it's not the owner
		if(!player.username.equalsIgnoreCase(cashRegister.getOwner()) || player.isSneaking()) {
			cashRegister.attemptBuy(player);
		} else {
			player.openGui(Main.instance, 0, world, x, y, z);
		}
		return true;	
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
		dropItems(world, x, y, z);
		super.breakBlock(world, x, y, z, par5, par6);
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public int getRenderType() {
		return RENDER_ID;
	}

	private void dropItems(World world, int x, int y, int z) {
		Random rand = new Random();

		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (!(tileEntity instanceof IInventory)) {
			return;
		}
		IInventory inventory = (IInventory) tileEntity;

		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack item = inventory.getStackInSlot(i);

			if (item != null && item.stackSize > 0) {
				float rx = rand.nextFloat() * 0.8F + 0.1F;
				float ry = rand.nextFloat() * 0.8F + 0.1F;
				float rz = rand.nextFloat() * 0.8F + 0.1F;

				EntityItem entityItem = new EntityItem(world, x + rx, y + ry, z
						+ rz, new ItemStack(item.itemID, item.stackSize,
						item.getItemDamage()));

				if (item.hasTagCompound()) {
					entityItem.func_92014_d().setTagCompound(
							(NBTTagCompound) item.getTagCompound().copy());
				}

				float factor = 0.05F;
				entityItem.motionX = rand.nextGaussian() * factor;
				entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
				entityItem.motionZ = rand.nextGaussian() * factor;
				world.spawnEntityInWorld(entityItem);
				item.stackSize = 0;
			}
		}
	}
	
	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		return new TileEntityCashRegister();
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entity) {
		if(entity instanceof EntityPlayer) {
			TileEntityCashRegister cashRegister = (TileEntityCashRegister)world.getBlockTileEntity(x, y, z);
			
			if(entity instanceof EntityPlayerMP) {
				cashRegister.setOwner(((EntityPlayerMP)entity).username);
			} else {
				cashRegister.setOwner(((EntityClientPlayerMP)entity).username);
			}
			
			cashRegister.setDirection(((EntityPlayer) entity).rotationYawHead);
			
		}
	}
	
}
