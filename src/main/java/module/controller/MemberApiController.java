package module.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import module.Member;
import module.service.MemberService;
import support.Response;
import support.ResponseCode;

// @RequiredArgsConstructor// 자동으로 생성자 주입
@RestController
@RequestMapping("/api/member")
public class MemberApiController {

    private final MemberService memberService;
    
    @Autowired
    public MemberApiController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/list")
    public ResponseEntity<Response<List<Member>>> list() {
        List<Member> members = memberService.findAll();
        System.out.println(members);
        return ResponseEntity
            .ok(new Response<>(ResponseCode.Ok, ResponseCode.Ok.name(), members));
    }

    // TODO: 회원 추가하기
    @PostMapping// postman에서 body > raw > json > {"name": "김길동","age": 12}
    public ResponseEntity<Response<Long>> addForm(@RequestBody Member member) {
    	Optional<Member> test = memberService.findByName(member.getName());
    	System.out.println(test.isPresent());
    	test.ifPresent(anything -> {
    		System.out.println(anything);
    	});
    	if(memberService.findByName(member.getName()).isPresent()) {// 이름이 중복된다면
    		return ResponseEntity
                    .ok(new Response<>(ResponseCode.Conflict, ResponseCode.Conflict.name()));
    	}
    	Long id = memberService.add(member.getName(), member.getAge());
    	return ResponseEntity
                .ok(new Response<>(ResponseCode.Ok, ResponseCode.Ok.name(), id));
    }

    // TODO: 회원 수정하기
    @PutMapping("{id}")
    public ResponseEntity<Response<Void>> edit(@PathVariable Long id, @RequestBody Member member) {
    	if(memberService.findById(id) == null) {
    		return ResponseEntity
                    .ok(new Response<>(ResponseCode.NotFound, ResponseCode.NotFound.name()));
    	}
    	memberService.update(id, member.getName(), member.getAge());
        return ResponseEntity
                .ok(new Response<>(ResponseCode.Ok, ResponseCode.Ok.name()));
    }

    // TODO: 회원 삭제하기
    @DeleteMapping("{id}")
    public ResponseEntity<Response<Void>> delete(@PathVariable Long id) {
    	if (memberService.findById(id) == null) {
            return ResponseEntity
                .ok(new Response<>(ResponseCode.NotFound, ResponseCode.NotFound.name()));
        }
        memberService.delete(id);
        return ResponseEntity
                .ok(new Response<>(ResponseCode.Ok, ResponseCode.Ok.name()));
    }

    // TODO: 회원 찾기
    @GetMapping("{id}")// http://localhost:8080/api/member/1
    public ResponseEntity<Response<Member>> findById(@PathVariable Long id) {
    	Member member = memberService.findById(id);
    	if (member == null) {
            return ResponseEntity
                .ok(new Response<>(ResponseCode.NotFound, ResponseCode.NotFound.name(), member));
        }
        return ResponseEntity
            .ok(new Response<>(ResponseCode.Ok, ResponseCode.Ok.name(), member));
    }

}
