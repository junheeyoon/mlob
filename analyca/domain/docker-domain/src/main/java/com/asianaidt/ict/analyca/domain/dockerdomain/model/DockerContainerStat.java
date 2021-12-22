package com.asianaidt.ict.analyca.domain.dockerdomain.model;

import lombok.Data;

@Data
public class DockerContainerStat {
    private String containerName;
    private String containerId;
    private String cpu;
    private String usedMem;
    private String totalMem;
    private String usedNetIn;
    private String totalNetOut;
    private String usedBlockIn;
    private String totalBlockOut;

    /**
     * docker stats --no-stream "--format={{.Name}}|{{.ID}}|{{.CPUPerc}}|{{.MemUsage}}|{{.NetIO}}|{{.BlockIO}}"
     * elated_benz|2ded92ad81e1|0.00%|4.648MiB / 31.22GiB|25.8kB / 0B|0B / 0B
     * youthful_banach|b2b45f0c53d8|0.06%|15.61MiB / 31.22GiB|571MB / 5.79GB|311kB / 0B
     * idc-registry|1873df9adf28|0.04%|7.309MiB / 31.22GiB|3MB / 2.36GB|2.04GB / 0B
     * 6260e26f7b8f_gitlab|6260e26f7b8f|8.18%|6.871GiB / 31.22GiB|5.56GB / 2.39GB|22.9GB / 217GB
     * gitlab-runner|250149ce7208|0.04%|6.148MiB / 31.22GiB|1.96GB / 5.35GB|129MB / 0B
     */
    public static DockerContainerStat fromJSON(final String json, final String delimiter) {
        if (json == null || delimiter == null) return null;
        DockerContainerStat dockerContainerStat = new DockerContainerStat();
        String[] stat = json.split(delimiter);

        dockerContainerStat.setContainerName(stat[0]);
        dockerContainerStat.setContainerId(stat[1]);
        dockerContainerStat.setCpu(stat[2].replace("%", ""));
        String[] memory = stat[3].split("/");
        dockerContainerStat.setUsedMem(memory[0].replace("MiB", ""));
        dockerContainerStat.setTotalMem(memory[1].replace("MiB", ""));
        return dockerContainerStat;
    }
}
