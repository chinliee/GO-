package com.team4.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.team4.demo.model.dto.menu.MenuCreateDto;
import com.team4.demo.model.dto.menu.MenuDeleteDto;
import com.team4.demo.model.dto.menu.MenuSearchRespDto;
import com.team4.demo.model.dto.menu.MenuUpdateDto;
import com.team4.demo.model.dto.response.ResponseDto;
import com.team4.demo.model.dto.restaurant.RestaurantAdmin;
import com.team4.demo.model.dto.restaurant.RestaurantContactInfo;
import com.team4.demo.model.dto.restaurant.RestaurantDataRespDto;
import com.team4.demo.model.dto.restaurant.RestaurantFindAllDto;
import com.team4.demo.model.dto.restaurant.RestaurantIntroduce;
import com.team4.demo.model.dto.restaurant.RestaurantRegisterDto;
import com.team4.demo.model.dto.restaurant.RestaurantResponseDto;
import com.team4.demo.model.dto.restaurant.RestaurantUpdateInfoDto;
import com.team4.demo.model.dto.restaurant.Restaurantlogin;
import com.team4.demo.model.entity.Restaurant;
import com.team4.demo.service.RestaurantService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    /**
     * 外送、外帶餐廳頁面，取得餐廳、餐點資料
     * @param restaurantId，餐廳 ID
     * @return {@link ResponseEntity}<{@link ResponseDto}>
     */
    @GetMapping("/restaurant/data")
    public ResponseEntity<ResponseDto> getRestaurantData(@RequestParam Integer restaurantId) {
        log.info("RestaurantController - getRestaurantData ... restaurantId => {}", restaurantId);
        RestaurantDataRespDto respDto = restaurantService.getRestaurantData(restaurantId);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK.value(), respDto));
    }

    @GetMapping("/restaurant/menu/list")
    public ResponseEntity<ResponseDto> listMenu(@RequestParam Integer restaurantId) {
        log.info("RestaurantController - listMenu ... restaurantId => {}", restaurantId);
        List<MenuSearchRespDto> dtoList = restaurantService.getRestaurantMenu(restaurantId);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK.value(), dtoList));
    }

    @PostMapping("/restaurant/menu/create")
    public ResponseEntity<ResponseDto> createMenu(@ModelAttribute MenuCreateDto createDto) {
        log.info("RestaurantController - createMenu ... createDto => {}", createDto);
        MenuSearchRespDto respDto = restaurantService.createMenu(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(HttpStatus.CREATED.value(), respDto));
    }

    @PostMapping("/restaurant/menu/update")
    public ResponseEntity<ResponseDto> updateMenu(@ModelAttribute MenuUpdateDto updateDto) {
        log.info("RestaurantController - updateMenu ... updateDto => {}", updateDto);
        MenuSearchRespDto respDto = restaurantService.updateMenu(updateDto);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK.value(), respDto));
    }

    @PostMapping("/restaurant/menu/delete")
    public ResponseEntity<ResponseDto> deleteMenu(@RequestBody MenuDeleteDto deleteDto) {
        log.info("RestaurantController - deleteMenu ... deleteDto => {}", deleteDto);
        restaurantService.deleteMenu(deleteDto);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK.value()));
    }
    @PostMapping("/re")
	public String login(@RequestBody Restaurantlogin dto) {
		
	return restaurantService.findbyuniformnumbers(dto);
	}	

	@PostMapping("/Restaurant")
	public List<RestaurantAdmin> FinlAll(){
		
	return restaurantService.AdminFindAll();
	}

	@GetMapping("/restaurant/{restaurantId}")
	public RestaurantFindAllDto findByID(@PathVariable("restaurantId") Integer restaurantId) {
		
	return restaurantService.findById(restaurantId);
	}

	@PutMapping("/restaurant")
	public Restaurant insert(@RequestBody RestaurantRegisterDto dto) {
		
	return restaurantService.insertRestaurant(dto);
	}

	@PutMapping("/restaurantupdata/Contact/{restaurantId}")
	public String updateRestaurantContactInfo(@PathVariable("restaurantId") Integer restaurantId, 
											  @RequestBody RestaurantContactInfo dto) {
	
	restaurantService.updateRestaurantContactInfo(restaurantId, dto);
		
	return "修改成功";
	}

	@PutMapping("/restaurantupdata/restaurantInfo/{restaurantId}")
	public String  updaterestaurantInfo(@PathVariable("restaurantId") Integer restaurantId,
										@RequestBody RestaurantUpdateInfoDto dto) {
		
	restaurantService.updateRestaurantInfo(restaurantId, dto);
	
	return "修改成功";
	}
	

	@PutMapping("/restaurantupdata/restaurantIntroduce/{restaurantId}")
	public String updateRestaurantIntroduce(@PathVariable("restaurantId") Integer restInteger,
											@RequestBody RestaurantIntroduce dto) {
		
	restaurantService.updateRestaurantIntroduce(restInteger, dto);
	
	return "OK";		
	}
	
	@PutMapping("/restaurantupdata/{restaurantId}/name")
	public String updateRestaurantName(@PathVariable("restaurantId") Integer restaurantId, @RequestParam("name") String name) {
	
	restaurantService.updateRestaurantName(restaurantId, name);
	
	return "OK";
	}

	@PutMapping("/restaurant/register")
	public Restaurant registerRestaurant(@RequestBody RestaurantRegisterDto dto) {
	
	        
	return restaurantService.insertRestaurant(dto);
	}

	@PostMapping("/login")
	public  ResponseEntity<?> login1(@RequestBody Restaurantlogin restaurantlogin) {
	    String username = restaurantlogin.getUniformNumbers();
	    String password = restaurantlogin.getPassword();
			    
	    List<String> isAuthenticated = restaurantService.authenticateAndGetRestaurantId(username, password);

	    if (isAuthenticated != null) {
            String restaurantId = isAuthenticated.get(0);
            String restaurantName = isAuthenticated.get(1);
            RestaurantResponseDto dto = new RestaurantResponseDto(restaurantId, restaurantName);
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("統編與密碼不匹配");
        }
	}
	

	
	
	}       
	
