package org.investovator.ui.dataplayback.gametype;

import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.dataplaybackengine.player.type.PlayerTypes;

import java.util.ArrayList;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public enum GameTypes implements GameConfiguration {

    DAILY_SUMMARY_CLOSING_PRICE_GAME{
        @Override
        public ArrayList<TradingDataAttribute> getInterestedAttributes() {
            ArrayList<TradingDataAttribute> attrs=new ArrayList<>();
            attrs.add(TradingDataAttribute.DAY);
            attrs.add(TradingDataAttribute.CLOSING_PRICE);
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
    },

    TICKER_DATA_GAME{
        @Override
        public ArrayList<TradingDataAttribute> getInterestedAttributes() {
            ArrayList<TradingDataAttribute> attrs=new ArrayList<>();
            attrs.add(TradingDataAttribute.DAY);
            attrs.add(TradingDataAttribute.PRICE);

            return attrs;
        }

        @Override
        public PlayerTypes getPlayerType() {
            return PlayerTypes.REAL_TIME_DATA_PLAYER;
        }



        @Override
        public TradingDataAttribute getAttributeToMatch() {
            return TradingDataAttribute.PRICE;
        }

        @Override
        public String getDescription() {
            return "Ticker data based game traded prices of stocks";
        }
    }

    ;


}
