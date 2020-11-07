package countminsketch;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.List;

public class CMS extends KeyedProcessFunction<Long, String, Integer>{


    int rowCount=5;
    int columncount=12;

    ValueState<List<CMSRow>> cmsTable;

    @Override
    public void open(Configuration c){
        List<CMSRow> cmst= new ArrayList<>();
        for(int i=1;i<rowCount;++i)
            cmst.add(new CMSRow(columncount));


        ValueStateDescriptor<List<CMSRow>> cmsTD = new ValueStateDescriptor<List<CMSRow>>("desc",  TypeInformation.of(new TypeHint<List<CMSRow>>() {
        }),cmst );
        cmsTable = getRuntimeContext().getState(cmsTD);


    }

    @Override
    public void processElement(String value, Context ctx, Collector<Integer> out) throws Exception {
        List<CMSRow> cmst = this.cmsTable.value();

        for (CMSRow row :cmst){
            row.hash(value);

        }

        int min = cmst.get(0).query(value);
        for (CMSRow r : cmst) {
            int estimate = r.query(value);
            if (min > estimate)
                min = estimate;
        }
        cmsTable.update(cmst);
        System.out.println(value+ " : "+min);
        out.collect(min);


    }
}
