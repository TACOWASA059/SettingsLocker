package com.github.tacowasa059.settingslocker.mixin;

import net.minecraft.client.OptionInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(OptionInstance.UnitDouble.class)
public abstract class EnumUnitDoubleMixin<T> {

    @Inject(method = "validateValue(Ljava/lang/Double;)Ljava/util/Optional;", at=@At("HEAD"), cancellable = true)
    public void validateValue(Double p_231747_, CallbackInfoReturnable<Optional<Double>> cir) {
        cir.setReturnValue(p_231747_ >= 0.0D && p_231747_ <= 100D ? Optional.of(p_231747_) : Optional.empty());
        cir.cancel();
    }

}

