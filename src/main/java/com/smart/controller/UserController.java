package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.userRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private userRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
   @ModelAttribute
   public void addCommonData(Model model,Principal principal)
   {
	    String userName=principal.getName();
		System.out.println("userName  " +userName);
		//getting the user using username(email)
		User user = userRepository.getUserByUserName(userName);
		System.out.println("user  " +user);
		
		//sending user to user_dashboard page 
		model.addAttribute("user",user);
		System.out.println("user " +user);
   }
	
	@RequestMapping("/index")
	public String dashboard(Model model,Principal principal)
	{   
		//get the username email
		String userName=principal.getName();
		System.out.println("userName  " +userName);
		//getting the user using username(email)
		User user = userRepository.getUserByUserName(userName);
		System.out.println("user  " +user);
		
		//sending user to user_dashboard page 
		model.addAttribute("user",user);
		
		return "normal/user_dashboard";
	}
	
	@GetMapping("/add-contact")
	public String openAddContactform(Model model)
	{
		
		model.addAttribute("title","Add contact");
		model.addAttribute("contact",new Contact());
		return "normal/add_contact_form";
		
	}
	
	
	//process contact form
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact,@RequestParam("profileImage") MultipartFile file,
			Principal principal,HttpSession session)
	{
		try {
		String name=principal.getName();
		User user =this.userRepository.getUserByUserName(name);
		//processing image
		if(file.isEmpty())
		{
			System.out.println("file is empty");
			contact.setImage("deepak.png");
		}
		else {
			contact.setImage(file.getOriginalFilename());
			
			File saveFile=new ClassPathResource("static/img").getFile();
			Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			Files.copy(file.getInputStream(), path,StandardCopyOption.REPLACE_EXISTING);
			System.out.println("file is uploaded");
			
		}
		contact.setUser(user);
		user.getContacts().add(contact);
		
		this.userRepository.save(user);
		//System.out.println("Data "+contact); 
		System.out.println("added to database");
		
		//message success
		session.setAttribute("message",new Message("your contact is added !! Add more","success"));
		//System.out.println(user);
		//System.out.println(contact);
		}catch (Exception e){
			System.out.println("error"+e.getMessage());
			e.printStackTrace();//it gives details 
			//message error btayenge agr kuch bhi exception aya to 
			session.setAttribute("message",new Message("something went wrong !! try again","danger"));
		}
		return "normal/add_contact_form";
	}
	
	
	
	//show contacts handler
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model m, Principal principal) {
		m.addAttribute("title","Show User Contacts");
		//hame yha se contact ki list bhejni he or vha par show karni he or db se contacts lekar se karna he hum esa karke contacts ki puri list form pr bhej skte he pr hum contact repository bna kr krunga kyuki hme bad me changes krna padega  String userName=principal.getName();
		
		//User user= this.userRepository.getUserByUserName(userName);
		//List<Contact> contacts =user.getContacts();
		
		String userName=principal.getName();//ye emailId he
		User user= this.userRepository.getUserByUserName(userName);//ye user de dega ab is user se userid
		//nikalenge;
		
		Pageable pageable =PageRequest.of(page,5);//ye hme pageable return kar dega
		
		Page <Contact> contacts =this.contactRepository.findContactsByUser(user.getId(),pageable);//user.getId gives userId
		
		
		//ye hamare pas list of userId ke contacts aa gye 
		
		//now we are sending contacts at form
		m.addAttribute("contacts",contacts);
		m.addAttribute("currentPage",page);
		m.addAttribute("totalPages",contacts.getTotalPages());//ye hme total page de dega  or divide bhi kar dega pages me form pr
		
		return "normal/show_contacts";
	}
	
	//showing single contact details
	@RequestMapping("/{id}/contact")
	public String showContactDetails(@PathVariable("id")Integer id,Model model,Principal principal)
	{
		System.out.println("contact id"+id);
		
		Optional<Contact> contactOptional=this.contactRepository.findById(id);
		Contact contact=contactOptional.get();
		//hamne loggedin user nikal liya
		String userName=principal.getName();
		User user =this.userRepository.getUserByUserName(userName);
		//ye humne check laga diya ki user ki id equal he contact view ho rha he us id se to hi chale
		if(user.getId()==contact.getUser().getId()) {
			
			model.addAttribute("contact",contact);
			model.addAttribute("title",contact.getName());
		}
		
		return "normal/contact_details";
	}
	
	//delete contact handler
	@GetMapping("/delete/{id}")
	public String deleteContact(@PathVariable("id")Integer id,Model m
			,HttpSession session,Principal principal) {
		
		
		Contact contact=this.contactRepository.findById(id).get();
		
		//check lagayenge-jo contact delte kar rha he uski id or uske contact ki id same hona chaiy ye hme kam karna he  
		System.out.println("contact"+contact.getId());
		
		//contact.setUser(null);
		
		//remove image 
		//img ka name contact.getImage se mil jayega 
		
		
		//this.contactRepository.delete(contact);
		//user me show contact vale sare user aa gye
		User user =this.userRepository.getUserByUserName(principal.getName());
		user.getContacts().remove(contact);
		
		this.userRepository.save(user);
		
		
		System.out.println("Deleted");
		
		session.setAttribute("message",new Message("contact deleted successfullY.!!","success"));
		
		return "redirect:/user/show-contacts/0";
	}
	
	//update contact details
	//hum post mapping use kar rhe he to hme button ko ese link karna he ki vah post request mare
	@PostMapping("/update-contact/{id}")
	public String updateForm(@PathVariable("id")Integer id ,Model m)
	{
		m.addAttribute("title","update contact");
		
		Contact contact=this.contactRepository.findById(id).get();
		
		m.addAttribute("contact",contact);
		
		return "normal/update_form";
	}
			
//update contact handler
@RequestMapping(value="/process-update",method=RequestMethod.POST)
public String updateHandler(@ModelAttribute Contact contact,
		@RequestParam("profileImage")MultipartFile file,
		Model m,HttpSession session,Principal principal)
{
	try {
		
		//delete old photo old photo ko delete krne ke liye hme purane contact ka data chaiye isliye hum jo id he contact ki usase fetch kar lenge 
		Contact oldContactDetails=this.contactRepository.findById(contact.getId()).get();
		
		if(!file.isEmpty())
		{
			//file work
			//rewrite
			
			//delete old photo
			
			File deleteFile=new ClassPathResource("static/img").getFile();
			File file1= new File(deleteFile,oldContactDetails.getImage());
			file1.delete();
			
			//update new photo 
			File saveFile=new ClassPathResource("static/img").getFile();
			Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			Files.copy(file.getInputStream(), path,StandardCopyOption.REPLACE_EXISTING);
			contact.setImage(file.getOriginalFilename());
		}
		else {
			//agr file empty he to hum purani vali image hi new contact me save kar denge
			contact.setImage(oldContactDetails.getImage());
		}
		User user =this.userRepository.getUserByUserName(principal.getName());
		contact.setUser(user);
		
		this.contactRepository.save(contact);
		
		//message de dete he
		session.setAttribute("message",new Message("Your contact is updated.!!","success"));
		
	}catch(Exception e){
		e.printStackTrace();
		
	}
	
	System.out.println("CONTACT NAME"+contact.getName());
	//contact ki id ni aayegi
	System.out.println("CONTACT ID"+contact.getId());
	return "redirect:/user/"+contact.getId()+"/contact";
}

//Your profile handler
@GetMapping("/profile")
public String yourProfile(Model m)
{
	m.addAttribute("title","Profile Page");
	return "normal/profile";
}

	
}






