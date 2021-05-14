package edu.miu.cs.cs544.controller;

import edu.miu.cs.cs544.auth.service.ITokenService;
import edu.miu.cs.cs544.auth.model.AuthenticationRequest;
import edu.miu.cs.cs544.auth.model.AuthenticationResponse;
import edu.miu.cs.cs544.dto.PersonRequest;
import edu.miu.cs.cs544.dto.PersonResponse;
import edu.miu.cs.cs544.service.IMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import edu.miu.cs.cs544.domain.Person;
import edu.miu.cs.cs544.service.IPersonService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/public")
public class PublicController {
	@Autowired
	private IPersonService personService;

	@Autowired
    private ITokenService tokenService;

	@Autowired
    private IMapService mapService;

  @PostMapping("/signup")
  public ResponseEntity<?> signUp(@Validated @RequestBody PersonRequest personRequest) {
      Person person = mapService.convertToEntity(personRequest);
      person = personService.createCustomer(person);
      PersonResponse personResponse = mapService.convertToDTO(person);
      return ResponseEntity.ok(personResponse);}

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@Validated @RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        String token = tokenService.generateToken(authenticationRequest);
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<?> logout(@Validated HttpServletRequest request, HttpServletResponse response) throws Exception {
      String successful = tokenService.logout(request) ? "Logout Successful" : "Logout Failed";
      return ResponseEntity.ok(successful);
    }

}
