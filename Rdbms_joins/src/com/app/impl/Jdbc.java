/**
 * 
 */
package com.app.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.app.model.Registration;
import com.app.model.Student;

/**
 * @author Rutuja
 *
 */
public class Jdbc {

	private boolean flag = Boolean.FALSE;
	Scanner sc = new Scanner(System.in);

	public void insert() {
		Connection con = JdbcUtility.getConnection();
		try (PreparedStatement ps = con.prepareStatement("insert into student (name,branch) values(?,?)");) {
			System.out.println("How many students want to add ?");
			int noOfStudent = sc.nextInt();
			for (int i = 0; i < noOfStudent; i++) {
				Student s = new Student();
				System.out.println("Enter name of Student");
				s.setName(sc.next());
				System.out.println("Enter branch of student");
				s.setBranch(sc.next());

				ps.setString(1, s.getName());
				ps.setString(2, s.getBranch());
				int result = ps.executeUpdate();
				if (result > 0) {
					PreparedStatement ps1 = con.prepareStatement("select max(id) from student");
					ResultSet rs = ps1.executeQuery();
					if (rs.next()) {
						s.setId(rs.getInt(1));

						Registration r = new Registration();
						PreparedStatement ps2 = con.prepareStatement("insert into registration (id,rollno)values(?,?)");
						System.out.println("Enter roll number");
						r.setRollno(sc.nextInt());
						s.setRegistration(r);           //HAS-A
						ps2.setInt(1, s.getId());
						ps2.setInt(2, s.getRegistration().getRollno());

						ps2.executeUpdate();
						flag = Boolean.TRUE;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (flag == Boolean.TRUE) {
			System.out.println("Inserted successfully...");
		} else {
			System.out.println("Failed...");
		}
	}

	public void select() {
		System.out.println();
		List<Student> list = new ArrayList<Student>();
		Connection con = JdbcUtility.getConnection();
		try (PreparedStatement ps = con
				.prepareStatement("select * from student s join registration r where s.id=r.id");) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Student s = new Student();
				s.setId(rs.getInt(1));
				s.setName(rs.getString(2));
				s.setBranch(rs.getString(3));
				Registration r = new Registration();
				r.setId(rs.getInt(4));
				r.setRollno(rs.getInt(5));

				s.setRegistration(r); // HAS-A

				list.add(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (Student s : list) {
			System.out.println(s.getId() + "\t" + s.getName() + "\t" + s.getBranch() + "\t"
					+ s.getRegistration().getId() + "\t" + s.getRegistration().getRollno());
		}
	}

}
