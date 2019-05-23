package domainV2.bet;

import javax.persistence.Entity;

import domainV2.bet.promotion.Promotion;

@Entity
public class BetDecorator extends BetSuperClass {

	protected BetSuperClass innerBet;
	private Promotion promo;

	public BetDecorator(Promotion promotion, BetSuperClass inner) {
		super(inner.question, inner.betAmount, inner.prediction, inner.user);
		innerBet = inner;
		question.removeObserver(this.innerBet);
		this.betAmount = inner.betAmount;
		promo = promotion;
		this.prediction = inner.prediction;
	}

	@Override
	public long betReturn() {
		finalPay = (long) (innerBet.betReturn() + promo.promoReturn(betAmount, question.getPayoff(prediction)));
		return (long) (innerBet.betReturn() + promo.promoReturn(betAmount, question.getPayoff(prediction)));
	}

}
