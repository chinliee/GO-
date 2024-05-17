package com.team4.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.team4.demo.model.dto.member.MemberCreateDto;
import com.team4.demo.model.dto.member.MemberGetAllMemberRespDto;
import com.team4.demo.model.dto.member.MemberGetMemberByEmailAndPasswordDto;
import com.team4.demo.model.dto.member.MemberGetMemberByEmailAndPasswordRespDto;
import com.team4.demo.model.dto.member.MemberGetMemberByEmailRespDto;
import com.team4.demo.model.dto.member.MemberRegisterDto;
import com.team4.demo.model.dto.member.MemberResponseDto;
import com.team4.demo.model.dto.member.MemberSearchByEmailRespDto;
import com.team4.demo.model.dto.member.MemberUpdateDto;
import com.team4.demo.model.dto.member.Memberlogin;
import com.team4.demo.model.dto.response.ResponseDto;
import com.team4.demo.model.entity.Member;
import com.team4.demo.service.MemberService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
public class MemberController {

	@Autowired
    private MemberService memberService;

    /**
     * 後臺餐點訂單頁面，複合查詢功能，根據 email 查詢會員
     * input 輸入同時，提供符合的 email 選項，達成 auto-complete 效果
     * @param email，email 關鍵字
     * @return {@link ResponseEntity}<{@link ResponseDto}>
     */
    @GetMapping("/member/search")
    public ResponseEntity<ResponseDto> searchMemberByEmail(@RequestParam String email) {
        log.info("MemberController - searchMemberByEmail ... email => {}", email);
        List<MemberSearchByEmailRespDto> respDtoList = memberService.searchMemberByEmail(email);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK.value(), respDtoList));
    }

    @GetMapping("/member/search/{email}")
    public ResponseEntity<ResponseDto> getMemberByEmail(@PathVariable("email") String email) {
        log.info("MemberColtroller - getMemberByEmail ... email => {}", email);
        List<MemberGetMemberByEmailRespDto> respDtoList = memberService.getMemberByEmail(email);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK.value(), respDtoList));
    }
    
    @GetMapping("/member/search/all-member")
    public ResponseEntity<ResponseDto> getAllMember() {
        log.info("MemberController - getAllMember ... without parameter");
        List<MemberGetAllMemberRespDto> respDtoList = memberService.getAllMember();
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK.value(), respDtoList));
    }

    @GetMapping("/member/search/all-menu-order")
    public ResponseEntity<ResponseDto> getMemberAllMenuOrder() {
        log.info("MemberColtroller - getMemberAllMenuOrder");
        // List<MemberGetAllMenuOrderRespDto> respDtoList = memberService.getMemberAllMenuOrder();
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK.value()/*, respDtoList*/));
    }

    @PostMapping("/member/search/email-and-password")
    public ResponseEntity<ResponseDto> getMemberEmailAndPassword(@RequestBody MemberGetMemberByEmailAndPasswordDto getDto) {
        MemberGetMemberByEmailAndPasswordRespDto respDto = memberService.getMemberByEmailAndPassword(getDto);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK.value(), respDto));
    }
    
    @DeleteMapping("/member/delete/{memberId}")
    public ResponseEntity<ResponseDto> deleteMemberById(@PathVariable("memberId") Integer memberId) {
        log.info("MemberController - deleteMemberById ... memberId => {}", memberId);
        memberService.deleteMemberById(memberId);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK.value()));
    }

    @PostMapping("/member/create")
    public ResponseEntity<ResponseDto> createMember(@RequestBody MemberCreateDto createDto) throws Exception {
        log.info("MemberController - createMember ... createDto => {}", createDto);
        memberService.createMember(createDto);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK.value()));
    }

    @PutMapping("member/update/{memberId}")
    public ResponseEntity<ResponseDto> putMethodName(@PathVariable Integer memberId, @RequestBody MemberUpdateDto updateDto) {
        log.info("MemberController - updateMember ... memberId => {}, updateDto => {}", memberId, updateDto);
        memberService.updateMember(memberId, updateDto);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK.value()));
    }



    @PostMapping("/member/register")
	public void registerMember(@RequestBody MemberRegisterDto dto) {
	memberService.insertMember(dto);
	}

    @PostMapping("/member/login")
	public  ResponseEntity<?> login1(@RequestBody Memberlogin memberlogin) {
	    String username = memberlogin.getEmail();
	    String password = memberlogin.getPassword();
		System.out.println(username);
		System.out.println(password);
	    List<String> isAuthenticated = memberService.authenticateAndGetMemberId(username, password);
        
		System.out.println(password);
	    if (isAuthenticated != null) {
            String memberId = isAuthenticated.get(0);
            String memberName = isAuthenticated.get(1);
            System.out.println(password);
            MemberResponseDto dto = new MemberResponseDto(memberId, memberName);
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("郵件與密碼不匹配");
        }
	}


























}
