package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.entity.Pict;
import com.example.demo.entity.Plant;
import com.example.demo.entity.TypePlant;
import com.example.demo.entity.User;
import com.example.demo.repository.PictRepository;
import com.example.demo.repository.PlantRepository;
import com.example.demo.repository.TypePlantRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.LoginService;

@Controller
public class PlantController {
	@Autowired
	private PictRepository pictRepository;
	@Autowired
	private PlantRepository plantRepository;
	@Autowired
	private TypePlantRepository typePlantRepository;
	@Autowired
	private LoginService loginService;

	@RequestMapping("/")
	public String index(Model model) {
		List<Plant> plantList = (List<Plant>) plantRepository.findAll();
		model.addAttribute("plantList", plantList);
		
		List<TypePlant> typeplantList = (List<TypePlant>) typePlantRepository.findAll();
		model.addAttribute("typeplantList", typeplantList);
		
		List<Pict> pictList = (List<Pict>) pictRepository.findAll();
		model.addAttribute("pictList", pictList);
		
		return "index";
	}
	
	@GetMapping("/login")
	public String loginForm(Model model) {
		User user = new User();
        model.addAttribute("user", user);
		return "login";
	}
	
    @PostMapping("/login")
    public String login(@ModelAttribute("user") User user ) {

    	User oauthUser = loginService.login(user.getUsername(), user.getPassword());
    	if(Objects.nonNull(oauthUser)) 
    	{	
    		return "redirect:/add_plant";
    	} else {
    		return "redirect:/login";
    	}
    }
    
    @GetMapping("/logout")
    public String logoutDo(HttpServletRequest request,HttpServletResponse response)
    {
        return "redirect:/login";
    }
	
	@GetMapping("/readmore/{id}")
	public String readMore(@PathVariable("id") int id, Model model) {
		Plant plant = plantRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid plant Id:" + id));
		Pict pict = pictRepository.findById((int) id).orElseThrow(() -> new IllegalArgumentException("Invalid Pict Id:" + id));
		List<TypePlant> typeplantList = (List<TypePlant>) typePlantRepository.findAll();
		
		model.addAttribute("typeplantList", typeplantList);
		model.addAttribute("plant", plant);
		model.addAttribute("pict", pict);
		
		return "readmore";
	}
	
	@GetMapping("/add_type")
	public String newType(Model model) {
		TypePlant typeplant = new TypePlant();
		List<TypePlant> typeList = typePlantRepository.findAll();
		
		model.addAttribute("typeList",typeList);
		model.addAttribute("typeplant",typeplant);
		return "new_type";
	}
	
	@PostMapping("/add_type")
	public String addType(@Validated TypePlant typeplant, BindingResult result, Model model) {
		typePlantRepository.save(typeplant);
		return "redirect:/add_type";
	}
	
	@PostMapping("/update_type/{typeplant_id}") 
	public String updateProduct(@PathVariable("typeplant_id") Integer id, @Validated TypePlant typeplant, BindingResult result, Model model) {
		typePlantRepository.save(typeplant);
		return "redirect:/add_type";
	}
	
	@GetMapping("/delete_type/{id}")
	public String deleteType(@PathVariable("id") long id, Model model) {
		TypePlant typeplant = typePlantRepository.findById((int) id).orElseThrow(() -> new IllegalArgumentException("Invalid type Id:" + id));
		typePlantRepository.delete(typeplant);
		return "redirect:/add_type";
	}
	
	@GetMapping("/add_plant")
	public String newPlant(Model model) {
		Plant plant = new Plant();
		model.addAttribute("plant", plant);
		
		Pict pict = new Pict();
		model.addAttribute("pict", pict);
		
		List<Plant> plantList = (List<Plant>) plantRepository.findAll();
		model.addAttribute("plantList", plantList);
		
		List<TypePlant> typeplantList = (List<TypePlant>) typePlantRepository.findAll();
		model.addAttribute("typeplantList", typeplantList);
		
		List<Pict> pictList = (List<Pict>) pictRepository.findAll();
		model.addAttribute("pictList", pictList);
		
		return "new_plant";
	}
	
	public static String uploadDirectory = System.getProperty("user.dir") + "/src/main/resources/static/img";
	
	@PostMapping("/add_plant")
	public String addPlant(@Validated Plant plant, @RequestParam("linkpict") MultipartFile files, BindingResult result, Model model) {

		StringBuilder filename = new StringBuilder();
        String name = plant.getName() + files.getOriginalFilename();
		
        Path fileNamePath = Paths.get(uploadDirectory,plant.getName()+ files.getOriginalFilename());
        filename.append(files.getOriginalFilename());
        
        
		try {
			Files.write(fileNamePath, files.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Pict pict = new Pict();
           
		plantRepository.save(plant);
		pict.setPlant(plant);
		pict.setLinkpict(name);
		pictRepository.save(pict);
		return "redirect:/add_plant";
	}
	
	@GetMapping("/delete_plant/{id}")
	public String deletePlant(@PathVariable("id") long id, Model model) {
		Pict pict = pictRepository.findById((int) id).orElseThrow(() -> new IllegalArgumentException("Invalid Pict Id:" + id));
		pictRepository.delete(pict);
		
		Plant plant = plantRepository.findById((int) id).orElseThrow(() -> new IllegalArgumentException("Invalid Plant Id:" + id));
		plantRepository.delete(plant);
		
	    Path fileToDeletePath = Paths.get(uploadDirectory +"/"+ pict);
	    try {
			Files.delete(fileToDeletePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "redirect:/add_plant";
	}
	
	@GetMapping("/edit_plant/{id}")
	public String showUpdateForm(@PathVariable("id") int id, Model model) {
		Plant plant = plantRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid plant Id:" + id));
		Pict pict = pictRepository.findById((int) id).orElseThrow(() -> new IllegalArgumentException("Invalid Pict Id:" + id));
		
		List<TypePlant> typeplantList = (List<TypePlant>) typePlantRepository.findAll();

		model.addAttribute("typeplantList", typeplantList);
		model.addAttribute("plant", plant);
		model.addAttribute("pict", pict);

		return "update_plant";
	}
	
	@PostMapping("/edit_plant/{id}")
	public String updatePlant(@PathVariable("id") Integer id,@Validated Plant plant, @RequestParam("linkpict") MultipartFile files, BindingResult result, Model model) {
		StringBuilder filename = new StringBuilder();
        String name = plant.getName() + files.getOriginalFilename();
		
        Path fileNamePath = Paths.get(uploadDirectory,plant.getName()+ files.getOriginalFilename());
        filename.append(files.getOriginalFilename());
        
        
		try {
			Files.write(fileNamePath, files.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Pict pict = pictRepository.findById((int) id).orElseThrow(() -> new IllegalArgumentException("Invalid pict Id:" + id));
           
		plantRepository.save(plant);

		pict.setLinkpict(name);
		pictRepository.save(pict);
		return "redirect:/add_plant";
	}
	
	
	
}
