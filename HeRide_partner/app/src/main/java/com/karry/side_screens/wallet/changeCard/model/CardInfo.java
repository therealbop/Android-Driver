package com.karry.side_screens.wallet.changeCard.model;

public class CardInfo
{

    private String card_image;
    private String card_numb;
    private String exp_month;
    private String exp_year;
    private String card_id,name, funding;
    private boolean defaultCard;

    public String getCard_image() {
        return card_image;
    }

    public CardInfo(String card_image, String card_numb,
                         String exp_month, String exp_year, String id, boolean defaultCard, String name, String funding) {
        super();
        this.card_image = card_image;
        this.card_numb = card_numb;
        this.exp_month = exp_month;
        this.exp_year = exp_year;
        this.card_id=id;
        this.defaultCard = defaultCard;
        this.name = name;
        this.funding = funding;
    }

    public String getCard_numb() {
        return card_numb;
    }
    public String getExp_month() {
        return exp_month;
    }
    public String getExp_year() {
        return exp_year;
    }
    public String getCard_id() {
        return card_id;
    }
    public boolean getDefaultCard() {
        return defaultCard;
    }
    public String getName() {
        return name;
    }
    public String getFunding() {
        return funding;
    }
}
