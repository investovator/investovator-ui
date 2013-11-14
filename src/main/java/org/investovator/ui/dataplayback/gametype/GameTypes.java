package org.investovator.ui.dataplayback.gametype;

import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.dataplaybackengine.player.type.PlayerTypes;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public enum GameTypes implements GameConfiguration {

    DAILY_SUMMARY_CLOSING_PRICE_GAME{
        @Override
        public TradingDataAttribute[] getInterestedAttributes() {
            TradingDataAttribute[] attrs=new TradingDataAttribute[2];
            attrs[0]=TradingDataAttribute.DAY;
            attrs[1]=TradingDataAttribute.CLOSING_PRICE;

            return attrs;
        }

        @Override
        public PlayerTypes getPlayerType() {
            return PlayerTypes.DAILY_SUMMARY_PLAYER;        }

        @Override
        public TradingDataAttribute getAttributeToMatch() {
            return TradingDataAttribute.CLOSING_PRICE;
        }

        @Override
        public String getDescription() {
            return "Daily Summary game based on closing prices of stocks";
        }
    }

    ;


}
