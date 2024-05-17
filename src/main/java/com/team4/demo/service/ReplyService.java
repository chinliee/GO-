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

import com.team4.demo.model.dto.reply.ReplyCreateDto;
import com.team4.demo.model.dto.reply.ReplyUpdateDto;
import com.team4.demo.model.entity.Reply;
import com.team4.demo.model.repository.ReplyRepository;


@Service
@Transactional
public class ReplyService {
	
	@Autowired
	private ReplyRepository replyRepository;
	
	public List<Reply> getAllreplys(){
		List<Reply> replys = replyRepository.findAll();
	
		return replys;
	}
	
	public void createReply(ReplyCreateDto createDto) {
		Reply reply = new Reply();
		reply.setContactadminId(createDto.getContactadminId());
		reply.setEmail(createDto.getEmail());
		reply.setContenttext(createDto.getContenttext());
		reply.setReplyTime(createDto.getReplyTime());
		
		replyRepository.save(reply);
	}
	
	public void updateReply(Integer reply_id, ReplyUpdateDto updateDto) {
		Reply reply = replyRepository.findById(reply_id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "此 replyId => " + reply_id + " 不存在"));
		reply.setContactadminId(updateDto.getContactadminId());
		reply.setEmail(updateDto.getEmail());
		reply.setContenttext(updateDto.getContenttext());
		reply.setReplyTime(updateDto.getReplyTime());
		
		replyRepository.save(reply);
	}
	
	public void deleteReply(Integer reply_id) {
		replyRepository.deleteById(reply_id);
	}
	
	public void emailreply(Integer reply_id,String contenttext) {
		
		Reply reply=replyRepository.findById(reply_id).orElse(null);
				
		
		String to = reply.getEmail();
        System.out.println(to);

        String from = "ispantest01@gmail.com";
        
  
        String host = "smtp.gmail.com";
        
 
        String username = "ispantest01@gmail.com";
        String password = "basfujzvuwqjvszw";

        
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

	
