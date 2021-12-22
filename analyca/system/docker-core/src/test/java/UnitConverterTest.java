import com.asianaidt.ict.analyca.system.dockercore.util.UnitConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UnitConverterTest {
    private final String ContainerStat = "elated_benz|2ded92ad81e1|0.00%|4.648MiB / 31.22GiB|25.8kB / 0B|0B / 0B";

    @Test
    @DisplayName("숫자 변환 테스트")
    public void toDoubleTest() {
        String[] split = getContainerStatSplit("\\|");
        assertEquals(0.00d, UnitConverter.toDouble(split[2]));
    }

    @Test
    @DisplayName("단위 변환 테스트")
    public void toByteTest() {
        String[] split = getContainerStatSplit("\\|")[3].split("/");
        assertEquals(4873781.248d, UnitConverter.toByte(split[0]));
    }

    public String[] getContainerStatSplit(final String delimiter) {
        return ContainerStat.replaceAll("\\s", "").split(delimiter);
    }
}