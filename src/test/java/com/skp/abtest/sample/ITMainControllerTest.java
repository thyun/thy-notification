package com.skp.abtest.sample;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ITMainControllerTest {

	@LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

// 	private String PLAB_UID = "84fdefe9-d46d-4191-8cc7-00b09e91a45f";

    @Test
    public void getIndex() throws Exception {

    }

/*    @Test
    public void getDetail() throws Exception {
		// Get /detail
    	HttpHeaders headers = new HttpHeaders();
    	headers.add("Cookie", "plab.uid" + PLAB_UID);
    	HttpEntity requestEntity = new HttpEntity(null, headers);
    	ResponseEntity responseEntity = restTemplate.exchange(
    		"http://localhost:" + port + "/detail",
    		HttpMethod.GET, requestEntity, String.class);

    	assertThat(responseEntity.getStatusCode(), CoreMatchers.is(HttpStatus.OK));
//	    assertThat((String) responseEntity.getBody(), CoreMatchers.containsString("Variation: A"));

//    		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/detail",
//                String.class)).contains("Variation: A");
    } */

}
