/* Profiles.java

	Purpose:
		
	Description:
		
	History:
		May 30, 2013 11:42:43 AM , Created by Sam

Copyright (C) 2013 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
 */
package demo;

import java.util.ArrayList;

public class Profiles {
	private Profiles() {}
	
	private static ArrayList<Profile> data;
	
	public final static ArrayList<Profile> provideData() {
		if (data != null) {
			return data;
		}
		data = new ArrayList<Profile>();
		
		data.add(new Profile("John", 1980, 1, 20, true, new Skill[]{Skill.AJAX}));
		data.add(new Profile("Mary", 1992, 2, 11, true, new Skill[]{Skill.Java}));
		data.add(new Profile("David", 2009, 3, 21, true, new Skill[]{Skill.Erlang, Skill.Haskell}));
		data.add(new Profile("Peter", 2010, 2, 15, true, new Skill[]{Skill.HTML, Skill.JavaScript}));
		data.add(new Profile("Michael", 2013, 5, 15, false, new Skill[]{Skill.Dart, Skill.JavaScript, Skill.HTML, Skill.CSS}));
		data.add(new Profile("Andy", 1981, 4, 5, true, new Skill[]{Skill.C, Skill.Java, Skill.JavaScript}));
		data.add(new Profile("Kevin", 1998, 10, 9, false, new Skill[]{Skill.Ruby, Skill.Lisp}));
		data.add(new Profile("Alex", 2003, 3, 20, true, new Skill[]{Skill.Haskell}));
		data.add(new Profile("Allen", 1985, 5, 20, false, new Skill[]{Skill.CSS, Skill.HTML}));
		data.add(new Profile("Amy", 2013, 4, 10, false, new Skill[]{Skill.CSS}));
		data.add(new Profile("Lisa", 1987, 7, 17, false, new Skill[]{Skill.CSS, Skill.HTML}));
		data.add(new Profile("Vicky", 2014, 6, 5, false, new Skill[]{Skill.Java, Skill.JavaScript, Skill.HTML}));
		data.add(new Profile("Emily", 2015, 3, 3, false, new Skill[]{Skill.Dart, Skill.JavaScript, Skill.HTML, Skill.CSS}));
		data.add(new Profile("Angela", 2007, 6, 3, false, new Skill[]{Skill.Erlang, Skill.Haskell, Skill.Lisp}));
		data.add(new Profile("Amanda", 1984, 5, 8, false, new Skill[]{Skill.Java, Skill.AJAX}));
		data.add(new Profile("Peggy", 2015, 6, 8, true, new Skill[]{Skill.JavaScript, Skill.HTML}));
		
		return data;
	}
}
