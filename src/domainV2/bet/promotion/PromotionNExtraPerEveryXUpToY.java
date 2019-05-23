package domainV2.bet.promotion;

public class PromotionNExtraPerEveryXUpToY implements Promotion {

	@SuppressWarnings("unused")
	private int extraToAdd;
	private int rate;
	private int limitAmount;

	@Override
	public double promoReturn(long originalBet, float payoff) {
		return ((originalBet + ((originalBet > limitAmount) ? limitAmount : (originalBet / rate))) * payoff)
				- (originalBet * payoff);
	}

	public PromotionNExtraPerEveryXUpToY(int extraToAdd, int rate, int limitAmount) {
		this.extraToAdd = extraToAdd;
		this.rate = rate;
		this.limitAmount = limitAmount;
	}

}
