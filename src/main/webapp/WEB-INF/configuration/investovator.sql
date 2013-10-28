-- investovator, Stock Market Gaming framework
-- Copyright (C) 2013  investovator
--
--     This program is free software: you can redistribute it and/or modify
--     it under the terms of the GNU General Public License as published by
--     the Free Software Foundation, either version 3 of the License, or
--     (at your option) any later version.
--
--     This program is distributed in the hope that it will be useful,
--     but WITHOUT ANY WARRANTY; without even the implied warranty of
--     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
--     GNU General Public License for more details.
--
--     You should have received a copy of the GNU General Public License
--     along with this program.  If not, see <http://www.gnu.org/licenses/>.

-- Database investovator_data
-- -------------------------------------------------------------------------

CREATE DATABASE investovator_data;

-- Table to store company information
CREATE TABLE IF NOT EXISTS investovator_data.COMPANY_INFO (
  SYMBOL varchar(5) NOT NULL,
  NAME varchar(20)  NOT NULL,
  SHARES int(10)  NOT NULL,
  PRIMARY KEY (SYMBOL)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Table to store users' stock watchlist ids
CREATE TABLE IF NOT EXISTS investovator_data.WATCH_LIST (
  USERNAME varchar(20) NOT NULL,
  SYMBOL varchar(5)  NOT NULL,
  PRIMARY KEY (USERNAME, SYMBOL)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Table to store users' stock watchlist ids
CREATE TABLE IF NOT EXISTS investovator_data.PORTFOLIO_VALUES (
  USERNAME varchar(20) NOT NULL,
  VALUE double NOT NULL,
  BLOCKED_VALUE double NOT NULL,
  PRIMARY KEY (USERNAME)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;