package com.team4.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.team4.demo.model.dto.member.MemberCreateDto;
import com.team4.demo.model.dto.member.MemberGetAllMemberRespDto;
import com.team4.demo.model.dto.member.MemberGetMemberByEmailAndPasswordDto;
import com.team4.demo.model.dto.member.MemberGetMemberByEmailAndPasswordRespDto;
import com.team4.demo.model.dto.member.MemberGetMemberByEmailRespDto;
import com.team4.demo.model.dto.member.MemberRegisterDto;
import com.team4.demo.model.dto.member.MemberSearchByEmailRespDto;
import com.team4.demo.model.dto.member.MemberUpdateDto;
import com.team4.demo.model.entity.Member;
import com.team4.demo.model.repository.MemberRepository;
import com.team4.demo.util.SaltGenerator;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public List<MemberSearchByEmailRespDto> searchMemberByEmail(String email) {
        List<Member> memberList = memberRepository.findTop6ByEmailContaining(email);
        List<MemberSearchByEmailRespDto> respDtoList = memberList.stream()
                .map(member -> {
                    MemberSearchByEmailRespDto respDto = new MemberSearchByEmailRespDto();
                    BeanUtils.copyProperties(member, respDto);
                    return respDto;
                })
                .collect(Collectors.toList());

        return respDtoList;
    }

        public List<MemberGetMemberByEmailRespDto> getMemberByEmail(String email) {
        List<Member> memberList = memberRepository.findMemberByEmail(email);
        List<MemberGetMemberByEmailRespDto> respDtoList = memberList.stream()
            .map(member -> {
                MemberGetMemberByEmailRespDto respDto = new MemberGetMemberByEmailRespDto();
                BeanUtils.copyProperties(member, respDto);
                return respDto;
            })
            .collect(Collectors.toList());

        return respDtoList;
    }

    public List<MemberGetAllMemberRespDto> getAllMember() {
        List<Member> memberList = memberRepository.findAll();
        List<MemberGetAllMemberRespDto> respDtoList = memberList.stream()
            .map(member -> {
                MemberGetAllMemberRespDto respDto = new MemberGetAllMemberRespDto();
                BeanUtils.copyProperties(member, respDto);
                return respDto;
            })
            .collect(Collectors.toList());

        return respDtoList;
    }
    
    public void deleteMemberById(Integer memberId) {
        memberRepository.findById(memberId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "此 memberId => " + memberId + " 不存在"));
        // 防止干擾到其他人演示, 所以不能刪掉資料
        // member.setExistence("0");
        // memberRepository.save(member);
        memberRepository.deleteById(memberId);
    }
    
    public void createMember(MemberCreateDto createDto) throws Exception{
        Member member = new Member();
        member.setEmail(createDto.getEmail());
        member.setPassword(createDto.getPassword());
        member.setName(createDto.getName());
        member.setPhone(createDto.getPhone());
        member.setCounty(createDto.getCounty());
        member.setDistrict(createDto.getDistrict());
        member.setAddress(createDto.getAddress());
        member.setStatus(createDto.getStatus());
        member.setPhoto(createDto.getPhoto());
        member.setRegisteration_date(createDto.getRegisteration_date());
        member.setBin(createDto.getBin());
        member.setPan(createDto.getPan());
        member.setLuhn(createDto.getLuhn());
        memberRepository.save(member);
    }
    
    public void updateMember(Integer memberId, MemberUpdateDto updateDto) {
        Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "此 memberId => " + memberId + " 不存在"));
        
        member.setEmail(updateDto.getEmail());
        member.setName(updateDto.getName());
        member.setPhone(updateDto.getPhone());
        member.setAddress(updateDto.getAddress());
        member.setPhoto(updateDto.getPhoto());
        memberRepository.save(member);
    }

    public MemberGetMemberByEmailAndPasswordRespDto getMemberByEmailAndPassword(MemberGetMemberByEmailAndPasswordDto getDto) {
        Member member = memberRepository.findMemberByEmailAndPassword(getDto.getEmail(), getDto.getPassword());
        if(member == null) {
            System.out.println("This Member isn\'t exist!!! Please try valid account!!!");
            return null;
        }else {
            System.out.println("Member comfirm!!! Now entering...");
            MemberGetMemberByEmailAndPasswordRespDto respDto = new MemberGetMemberByEmailAndPasswordRespDto();
            respDto.setEmail(getDto.getEmail());
            respDto.setPassword(getDto.getPassword());
            return respDto;
        }
    }

    public Member insertMember(MemberRegisterDto createDto) {
        String salt = SaltGenerator.generateSalt();
        Member member = new Member();
        member.setEmail(createDto.getEmail());
        String hashedPassword = SaltGenerator.hashPassword(createDto.getPassword(), salt);
        member.setPassword(hashedPassword);
        member.setSalt(salt);
        member.setName(createDto.getName());
        member.setPhone(createDto.getPhone());
        member.setCounty(createDto.getCounty());
        member.setDistrict(createDto.getDistrict());
        member.setAddress(createDto.getAddress());
        member.setRegisteration_date(createDto.getRegisteration_date());
        return memberRepository.save(member);
    }

    public List<String> authenticateAndGetMemberId(String username, String password) {
        Member member = memberRepository.findByEmail(username);
        System.out.println(member);
        if (member == null) {
            return null; // 或者你可以拋出一個異常或者返回一個錯誤消息
        }
        
        String storedPassword = member.getPassword();
        String storedSalt = member.getSalt();
        System.out.println(storedPassword);
        System.out.println(storedSalt);
        String hashedPassword = SaltGenerator.hashPassword(password, storedSalt);
        
        // 比較資料庫中儲存的密碼和輸入密碼的雜湊值
        if (hashedPassword.equals(storedPassword)) {
            ArrayList<String> memberBack = new ArrayList<>();
            // 如果驗證成功，返回餐廳 ID
            String memberId = Integer.toString(member.getMemberId());
            String memberName = member.getName();
            
            memberBack.add(memberId);
            memberBack.add(memberName);
            return memberBack;
        } else {
            return null; // 或者你可以拋出一個異常或者返回一個錯誤消息
        }
    }

}
