package com.nekokittygames.Thaumic.Tinkerer.common.items

import com.nekokittygames.Thaumic.Tinkerer.common.ThaumicTinkerer
import com.nekokittygames.Thaumic.Tinkerer.common.blocks.BlockUnstableIce
import com.nekokittygames.Thaumic.Tinkerer.common.core.misc.{ItemNBT, TTCreativeTab}
import com.nekokittygames.Thaumic.Tinkerer.common.libs.{LibItemNames, LibMisc}
import mantle.utils.ItemStackNBTWrapper
import net.minecraft.block.material.Material
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.{EnumParticleTypes, BlockPos}
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import thaumcraft.api.ThaumcraftMaterials
import thaumcraft.common.Thaumcraft
import thaumcraft.common.config.Config
import thaumcraft.common.items.armor.{Hover, ItemBootsTraveller}

/**
 * Created by katsw on 21/10/2015.
 */
object ItemCometBoots extends ItemTXBoots{


  setUnlocalizedName(LibItemNames.BOOTS_COMET)
  override def getArmorTexture(stack: ItemStack, entity: Entity, slot: Int, `type`: String): String = LibMisc.MOD_ID + ":textures/models/armor/bootsComet.png"



  override def initItem(fMLPreInitializationEvent: FMLPreInitializationEvent): Unit =
    {
      super.initItem(fMLPreInitializationEvent)
      MinecraftForge.EVENT_BUS.register(this)
    }

  @SubscribeEvent
  def playerJumps(event:LivingEvent.LivingJumpEvent) ={
    if(event.entity.isInstanceOf[EntityPlayer] && event.entity.asInstanceOf[EntityPlayer].inventory.armorItemInSlot(0)!=null && event.entity.asInstanceOf[EntityPlayer].inventory.armorItemInSlot(0).getItem==this)
      {
        event.entityLiving.motionY=event.entityLiving.motionY+0.2750000059604645D;
      }
  }

  @SubscribeEvent
  def livingTick(event:LivingEvent.LivingUpdateEvent) =
  {

    if(event.entity.isInstanceOf[EntityPlayer])
      {
        val player=event.entity.asInstanceOf[EntityPlayer]


        if(player.inventory.armorItemInSlot(0)!=null && player.inventory.armorItemInSlot(0).getItem==this)
          {
            val vector=player.getLook(1.0f)
            val item=player.inventory.armorItemInSlot(0)
            if(!ItemNBT.getItemStackTag(item).hasKey(RUNTICKS))
              ItemNBT.getItemStackTag(item).setInteger(RUNTICKS,0)

            for(x <- -5 until 6 ; z <- -5 until 6) {
              if (player.worldObj.getBlockState(new BlockPos(player.posX + x, player.posY - 1, player.posZ + z)).getBlock == Blocks.water &&
                player.worldObj.getBlockState(new BlockPos(player.posX + x, player.posY - 1, player.posZ + z)).getBlock.getMaterial == Material.water &&
                !player.isInWater && (Math.abs(x) + Math.abs(z) < 8)) {
                player.worldObj.setBlockState(new BlockPos(player.posX + x, player.posY - 1, player.posZ + z), BlockUnstableIce.getDefaultState())
                player.worldObj.spawnParticle(EnumParticleTypes.SNOWBALL,(player.posX + x), player.posY, (player.posZ + z), 0.0D, 0.025D, 0.0D)
              }
            }
            var ticks=ItemNBT.getItemStackTag(item).getInteger(RUNTICKS)

            val motion=Math.abs(player.motionX)+Math.abs(player.motionY)+Math.abs(player.motionZ)
            if(motion >0.1 || !player.onGround)
              {
                if(ticks<100)
                  ticks=ticks+1
              }
            else {
              ticks=0;
            }

            if(!player.isWet && motion>0.1)
              {
                player.worldObj.spawnParticle(EnumParticleTypes.SNOWBALL,(player.posX + Math.random()-0.5F), (player.getEntityBoundingBox.minY + 0.25F + ((Math.random()-0.5)*0.25F)), (player.posZ + Math.random()-0.5F), 0.0D, 0.025D, 0.0D)
                player.worldObj.spawnParticle(EnumParticleTypes.SNOWBALL,(player.posX + Math.random()-0.5F), (player.getEntityBoundingBox.minY + 0.25F + ((Math.random()-0.5)*0.25F)), (player.posZ + Math.random()-0.5F), 0.0D, 0.025D, 0.0D)
              }
            ItemNBT.getItemStackTag(item).setInteger(RUNTICKS,ticks)
          }
      }
  }

}
