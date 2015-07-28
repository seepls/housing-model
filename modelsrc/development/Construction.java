package development;

import contracts.SaleMarketOffer;
import contracts.TangibleAsset;
import contracts.DepositAccount;


public class Construction extends EconAgent implements IHouseOwner {
	IMessage.IReceiver saleMarket;
	public double 	housesPerHousehold; 	// target number of houses per household
	public int 		housingStock;			// total number of houses built
	
	public Construction() {
		super(new DepositAccount.Owner(),
				new SaleMarketOffer.Issuer(),
				new TangibleAsset.Owner());
		housesPerHousehold = 4100.0/5000.0;
		housingStock = 0; 
	}
	
	@Override
	public void start(IModelNode parent) {
		saleMarket = parent.mustFind(HouseSaleMarket.class);
		parent.get(Bank.class).openAccount(get(DepositAccount.Owner.class));
		super.start(parent);
	}
	
	public void init() {
		housingStock = 0;		
	}
	
	public void step() {
		int targetStock = (int)(get(NodeGroup.class).size()*housesPerHousehold);
		// TODO: work out how to refer to different NodeGroups
		int shortFall = targetStock - housingStock;
		while(shortFall > 0) {
			buildHouse();
			--shortFall;
		}
	}
	
	public void buildHouse() {
		House newBuild;
		long price;

		newBuild = new House(parent.find(HouseSaleMarket.class), parent.find(RentalMarket.class));
		get(TangibleAsset.Owner.class).receive(newBuild);
		++housingStock;
		price = Data.HousingMarket.referenceSalePrice(newBuild.quality);
		get(SaleMarketOffer.Issuer.class).issue(newBuild, price, saleMarket);
	}
	
	@Override
	public boolean remove(House house) {
		return(true);
	}
}
