package com.grallandco.employee.service.rest.impl;

import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import com.grallandco.employee.service.converter.EmployeeConverter;
import com.grallandco.employee.service.model.Employee;

@Path("/hr/")
public class EmployeeResource {

	@GET
	@Produces({"application/xml", "application/json"})
	@Path("/employee/{employeeEmail}/") 
	public EmployeeConverter getEmployee( @PathParam ("employeeEmail") String email) {
		
		//dummy code
		Employee emp = new Employee();
		emp.setEmail(email);
		emp.setFirstName("John");
		emp.setLastName("Doe");
		EmployeeConverter converter = new EmployeeConverter(emp);
		return converter;
	}
}