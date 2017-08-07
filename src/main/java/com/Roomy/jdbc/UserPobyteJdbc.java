package com.Roomy.jdbc;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.Roomy.Util.Response;
import com.Roomy.Util.ResponseStatus;
import com.fasterxml.jackson.core.JsonProcessingException;

@Component
public class UserPobyteJdbc {
	
	@Autowired
	EntityManager entityManager;
	
	public Response responseMessage;


	public boolean validateJwtToken(String jwtToken) throws JsonProcessingException, SQLException {
		
		StoredProcedureQuery sp = entityManager.createStoredProcedureQuery("VALIDATE_JWTTOKEN");
							
		sp.registerStoredProcedureParameter("USER_TOKEN_VALUE", String.class, ParameterMode.IN);
		
		sp.setParameter("USER_TOKEN_VALUE", jwtToken);
		boolean result = sp.execute();
		if(result == true){
			List<Object[]> resultList = sp.getResultList();
		}
		
		return result;
	}
	
	public Response getJwtToken(String mobileNumber)  throws JsonProcessingException, SQLException {
		
		StoredProcedureQuery sp = entityManager.createStoredProcedureQuery("GET_TOKEN");
		sp.registerStoredProcedureParameter("CONTACT_NUMBER", String.class, ParameterMode.IN);
			System.out.println(mobileNumber);
		if (mobileNumber != null) {
			sp.setParameter("CONTACT_NUMBER", mobileNumber);
		}
		System.out.println(mobileNumber);
		boolean result = sp.execute();
		System.out.println(result);
		if(result == true){
			String jwtToken = "";
			List<Object[]> resultList = sp.getResultList();
			System.out.println(resultList.size());
			for (Object[] results : resultList) {
				jwtToken = results[0].toString();
				}
			
		responseMessage =  new Response(ResponseStatus.SUCCESS_CODE,ResponseStatus.SUCESS_MESSAGE,jwtToken,"");
			}else{
		responseMessage =  new Response(ResponseStatus.FAILURE_CODE,"User is logged in.!",null,"");
			}
		
		
		
		return responseMessage;
	}
	


}
