package com.smart.controller;




import javax.servlet.http.HttpSession;
import javax.validation.Valid;


import org.springframework.beans.factory.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.smart.dao.userRepository;
import com.smart.entities.User;
import com.smart.helper.Message;




@Controller
public class HomeController {
	@Autowired
	private userRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;
	
	@RequestMapping("/home")
	
	public String home(Model model){
		
		model.addAttribute("title","home- smart contact manager");
		return "home";
				
	}
    @RequestMapping("/about")
	
	public String about(Model model){
		
		model.addAttribute("title","about - smart contact manager");
		return "about";
		
				
	}
    @RequestMapping("/signup")
	
   	public String signup(Model model){
   		
   		model.addAttribute("title","Register - smart contact manager");
   		model.addAttribute("user",new User()); 
   		return "signup";
   		
   				
   	}
    
    @RequestMapping(value="/do_register",method=RequestMethod.POST)
    public String registerUser(@Valid @ModelAttribute("user") User user,BindingResult result1, @RequestParam(value="agreement",defaultValue="false") boolean agreement,
    		Model model,  HttpSession session){
    	try {

        	if(!agreement)
        	{
        		System.out.println("you have not agreed terms and conditions");
        		throw new Exception(" you have not agreed terms and conditions");
        	}
        	
        	if(result1.hasErrors())
        	{
        		System.out.println("Errors" + result1.toString());   
        		model.addAttribute("user", user);
        		return "signup";
        	}
        		
        	user.setRole("ROLE_USER");
        	user.setEnabled(true);
        	user.setImageUrl("default.png");
        	user.setPassword(bcryptPasswordEncoder.encode(user.getPassword()));
        	User result=userRepository.save(user);
        	System.out.println("agreement"+"   "+agreement);
        	
        	System.out.println("USER"+"   "+user);
        	model.addAttribute(user);//if error comes then data phir se form pr chala jayega .
        	session.setAttribute("message",new Message("Successfully registered !!" ,"alert-success"));
        	return "signup";
        	
    	}
    	catch(Exception e){
    		e.printStackTrace();
    		
    		model.addAttribute("user",user);
    		session.setAttribute("message",new Message("Something went wrong !!" +e.getMessage(),"alert-danger"));
    		return "signup";
    		
    	}
    	
    	
    }
    
    
    //handler for custom login 
    @RequestMapping("/signin")
    public String customLogin(Model model)
    {
    	model.addAttribute("title","this is login page");
    	return "login";
    }

}
