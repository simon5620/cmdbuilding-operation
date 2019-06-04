package cz.cmdbuildingspringbootstarter.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author chenzhen
 * Created by chenzhen on 2019/5/24.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CmdbFilter {

    private String condition;

    private Object conditionValue;
}
