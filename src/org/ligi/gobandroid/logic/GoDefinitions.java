/**
 * gobandroid 
 * by Marcus -Ligi- Bueschleb 
 * http://ligi.de
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as 
 * published by the Free Software Foundation; 
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. 
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 **/

package org.ligi.gobandroid.logic;

public interface GoDefinitions {

	public final static byte PLAYER_BLACK=1;
	public final static byte PLAYER_WHITE=2;


	public final static byte STONE_NONE=0;
	public final static byte STONE_BLACK=1;
	public final static byte STONE_WHITE=2;

	
	public final static byte[][] hoshis19x19= { 
			 {15,3}, {3,15}, {15,15} , {3,3} , {9,9}
			,{3,9} , {15,9}, {9,3}   , {9,15}
		};
	
	public final static  byte[][] hoshis13x13= { 
			 {9,3}, {3,9}, {9,9} , {3,3} , {6,6}
			,{3,6} , {9,6}, {6,3}   , {6,9}
		};
	
	public final static byte[][] hoshis9x9= { 
			 {6,2}, {2,6}, {6,6} , {2,2} , {4,4}
			,{2,4} , {6,4}, {4,2}   , {4,6}
		};
	


}	
