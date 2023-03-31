import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AddItemByPLUTest.class, AddItemByScanningTest.class, AddOwnBagsTest.class, PurchaseBagsTest.class,
		WeightDiscrepancyTest.class })
public class FullTestSuite {

}
