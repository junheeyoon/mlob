package com.asianaidt.ict.analyca.system.dockercore.dto;

import com.asianaidt.ict.analyca.system.dockercore.type.DockerCommand;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter @Setter
@ToString
public class DockerCommandStompResponse {
    private DockerCommand.Type type;
    /**
     * 커맨드 요청에 의한 응답 변수.
     * type에 따라 응답은 다르므로 아래와 같이 정의하고 있다.
     * <p>
     * - RUN
     * - 0 : 실행 된 컨테이너 아이디 (컨테이너 아이디가 0일 경우 에러)
     * - START
     * - STOP
     * - RM
     *
     * @author leekn
     * @version 1.0.0 2020/05/19
     */
    private List<String> response;
    private Long idLogStep;
}
