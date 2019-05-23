package domainV2.bet.promotion;

public class PromotionNPercentageWinnings implements Promotion {

	private double percentage;

	@Override
	public double promoReturn(long originalBet, float payoff) {
		return (originalBet * (payoff + percentage)) - (originalBet * payoff);
	}

	public PromotionNPercentageWinnings(double percentage) {
		this.percentage = percentage;
	}

}
