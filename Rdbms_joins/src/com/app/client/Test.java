/**
 * 
 */
package com.app.client;

import com.app.impl.Jdbc;

/**
 * @author Rutuja
 *
 */
public class Test {

	public static void main(String[] args) {

		Jdbc j = new Jdbc();
		j.insert();

		j.select();
	}

}
