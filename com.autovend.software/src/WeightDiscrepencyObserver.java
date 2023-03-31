/*
 * Anna Lee: 30137463
 * Caleb Cavilla: 30145972
 * Desmond O'Brien: 30064340
 * Jose Perales: 30143354
 * Matthew Cusanelli: 30145324
 * Muhtadi Alam: 30150910
 * Omar Tejada: 31052626
 * Saadman Rahman: 30153482
 * Sahaj Malhotra: 30144405
 * Sean Tan: 30094560
 * Tanvir Ahamed Himel: 30148868
 * Victor campos: 30106934
 */
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.ElectronicScale;
import com.autovend.devices.observers.*;

public class WeightDiscrepencyObserver implements ElectronicScaleObserver {

	@Override
	public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToWeightChangedEvent(ElectronicScale scale, double weightInGrams) {
		
		
	}

	@Override
	public void reactToOverloadEvent(ElectronicScale scale) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToOutOfOverloadEvent(ElectronicScale scale) {
		// TODO Auto-generated method stub
		
	}

}
