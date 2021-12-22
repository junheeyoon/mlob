package com.asianaidt.ict.analyca.system.dockercore.dto;

import com.asianaidt.ict.analyca.system.dockercore.util.IPValidator;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.concurrent.ConcurrentHashMap;

public class DockerContainerResources extends ConcurrentHashMap<String, DockerContainerResources.DockerContainerResource> {
    @Setter
    @Getter
    @ToString
    public static class DockerContainerResource {
        // metrics
        private String containerName = "";
        private String containerId = "";
        private double cpu = 0;
        private double[] mem = {0, 0};
        private double[] netIO = {0, 0};
        private double[] blockIO = {0, 0};

        // ps
        private String creator = "";
        private String cpuCnt = "";
        private String memCnt = "";
        private String gpuCnt = "";
        private String createdAt = "";
        private String status = "";
        private String image = "";
        private String binds = "";
        private String ports = "";

        /**
         * 컨테이너 생성 시 사용 된 이미지 이름을 반환한다.
         *
         * @return 이미지 이름
         */
        public String getImageName() {
            String imageName = image;
            String[] split = image.split(":");
            if (split.length == 2) {
                // case 1. 127.0.0.1:1111/image -> Private Registry
                // case 2. image:tag -> Normal Image
                if (IPValidator.validate(split[0])) imageName = image;
                else imageName = split[0];
            } else if (split.length == 3) {
                // case 1. 127.0.0.1:1111/image:tag
                imageName = split[0] + ":" + split[1];
            }
            return imageName;
        }

        /**
         * 컨테이너 생성 시 사용 된 이미지의 태그를 반환한다.
         *
         * @return 이미지 태그
         */
        public String getImageTag() {
            String tagName = "";
            String[] split = image.split(":");
            if (split.length == 2) {
                // case 1. 127.0.0.1:1111/image -> Private Registry
                // case 2. image:tag -> Normal Image
                if (IPValidator.validate(split[0])) tagName = "";
                else tagName = split[1];
            } else if (split.length == 3) {
                // case 1. 127.0.0.1:1111/image:tag
                tagName = split[2];
            }
            return tagName;
        }
    }
}
