package br.ufes.inf.lied.controllers.test;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.ufes.inf.lied.objects.test.Greeting;
import br.ufes.inf.lied.objects.test.GreetingRequest;

@RestController
public class GreetingController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	// @RequestMapping( "/greeting" )
	// public Greeting greeting( @RequestParam( value = "name", defaultValue = "World" ) String name ) {
	// return new Greeting( counter.incrementAndGet(), String.format( template, name ) );
	// }

	@RequestMapping( path = "/greeting", method = RequestMethod.POST, consumes = "application/json" )
	public ResponseEntity<Greeting> greeting( @RequestBody GreetingRequest name ) {
		return new ResponseEntity<Greeting>( new Greeting( counter.incrementAndGet(), String.format( template, name.getName() ) ), HttpStatus.OK );
		// Greeting( counter.incrementAndGet(), String.format( template, name ) );
	}
	
	@RequestMapping( path = "/greeting2", method = RequestMethod.POST, consumes = "application/json" )
	@ResponseBody
	public Greeting greeting2( @RequestBody GreetingRequest name ) {
		return new Greeting( counter.incrementAndGet(), String.format( template, name.getName() ) );
	}

}
