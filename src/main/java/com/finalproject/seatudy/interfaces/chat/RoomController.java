package com.finalproject.seatudy.interfaces.chat;

import com.finalproject.seatudy.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
@Slf4j
public class RoomController {

    private final ChatRoomService chatRoomService;

    // 채팅방 목록 조회
    @GetMapping("/rooms")
    public ModelAndView rooms() {
        log.info("# All Chat Rooms");
        ModelAndView mv = new ModelAndView("chat/rooms");

        mv.addObject("list", chatRoomService.findAllRooms());

        return mv;
    }

    @PostMapping("/room")
    public String create(@RequestParam String name, RedirectAttributes rttr) {

        log.info("# Create Chat Room, name: " + name);
        rttr.addFlashAttribute("roomName", chatRoomService.createChatRoomDto(name));
        return "redirect:/chat/rooms";
    }

    @GetMapping("/room")
    public void getRoom(String roomId, Model model) {
        log.info("# get Chat Room, roomId: " + roomId);
        model.addAttribute("room", chatRoomService.findRoomById(roomId));
    }


}
