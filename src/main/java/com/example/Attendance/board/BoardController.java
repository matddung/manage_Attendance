package com.example.Attendance.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
}
