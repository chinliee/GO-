package com.team4.demo.service;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.team4.demo.model.dto.contact.ContactCreateDto;
import com.team4.demo.model.dto.contact.ContactUpdateDto;
import com.team4.demo.model.entity.Contactadmin;
import com.team4.demo.model.entity.Reply;
import com.team4.demo.model.repository.ContactadminRepository;


@Service
@Transactional
public class ContactadminService {
	
	@Autowired
	private ContactadminRepository contactadminRepository;
	
	
	public List<Contactadmin> getAllcontacts(){
		List<Contactadmin> contactadmins = contactadminRepository.findAll();
	
		return contactadmins;
	}
	
	public void createContact(ContactCreateDto createDto) {
		Contactadmin contact = new Contactadmin();
		contact.setName(createDto.getName());
		contact.setEmail(createDto.getEmail());
		contact.setContenttext(createDto.getContenttext());
		contact.setStatus(createDto.getStatus());
		contact.setCreatedTime(createDto.getCreatedTime());

		
		contactadminRepository.save(contact);
	}
	public void updateContact(Integer contactadmin_id, ContactUpdateDto updateDto) {
		Contactadmin contact = contactadminRepository.findById(contactadmin_id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "此 contactadmin_id => " + contactadmin_id + " 不存在"));
		contact.setName(updateDto.getName());
		contact.setEmail(updateDto.getEmail());
		contact.setContenttext(updateDto.getContenttext());
		contact.setStatus(updateDto.getStatus());
		contact.setCreatedTime(updateDto.getCreatedTime());
		
		contactadminRepository.save(contact);
	}
	
	public void deleteContact(Integer contactadmin_id) {
		contactadminRepository.deleteById(contactadmin_id);
	}
	public void emailreply(Integer contactadmin_id,String contenttext) {
		
		Contactadmin contact=contactadminRepository.findById(contactadmin_id).orElse(null);
				
		
		String to = contact.getEmail();
        System.out.println(to);

        String from = "信箱";
        
  
        String host = "smtp.gmail.com";
        
 
        String username = "信箱";
        String password = "信箱";

        
        // 设置属性
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        // 取得設置屬性
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // 宣告MimeMessage 來裝session
            MimeMessage message = new MimeMessage(session);

           
            message.setFrom(new InternetAddress(from));

            
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // 主旨
            message.setSubject("覓食GO客服中心回覆通知");

            // 內容
            message.setText(contenttext);

            // 寄送內容
            Transport.send(message);
            System.out.println("有發送嗎。");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
