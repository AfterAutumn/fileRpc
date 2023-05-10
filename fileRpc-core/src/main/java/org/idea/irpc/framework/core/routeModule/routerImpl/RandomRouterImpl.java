package org.idea.irpc.framework.core.routeModule.routerImpl;

import org.idea.irpc.framework.core.routeModule.ChannelFutureService;
import org.idea.irpc.framework.core.registy.RegistryConfig;
import org.idea.irpc.framework.core.routeModule.IRouter;
import org.idea.irpc.framework.core.routeModule.Selector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.idea.irpc.framework.core.common.cache.CommonClientCache.*;

/**
 * 负载均衡-随机
 *
 * @Author jiangshang
 */
public class RandomRouterImpl implements IRouter {


    @Override
    public void refreshRouterArr(Selector selector) {
        //获取服务提供者的数目
        List<ChannelFutureService> channelFutureServices = CONNECT_MAP.get(selector.getProviderServiceName());
        ChannelFutureService[] channels = new ChannelFutureService[channelFutureServices.size()];
        //提前生成调用先后顺序的随机数组
        int[] result = createRandomIndex(channels.length);
        //生成对应服务集群的每台机器的调用顺序
        for (int i = 0; i < result.length; i++) {
            channels[i] = channelFutureServices.get(result[i]);
        }
        SERVICE_ROUTER_MAP.put(selector.getProviderServiceName(), channels);
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setServiceName(selector.getProviderServiceName());
        //更新权重
        IROUTER.updateWeight(registryConfig);
    }

    @Override
    public ChannelFutureService select(Selector selector) {
        return CHANNEL_FUTURE_POLLING_REF.getChannelFutureService(selector);
    }

    @Override
    public void updateWeight(RegistryConfig registryConfig) {
        //服务节点的权重
        List<ChannelFutureService> channelFutureServices = CONNECT_MAP.get(registryConfig.getServiceName());
        Integer[] weightArr = createWeightArr(channelFutureServices);
        Integer[] finalArr = createRandomArr(weightArr);
        ChannelFutureService[] finalChannelFutureServices = new ChannelFutureService[finalArr.length];
        for (int j = 0; j < finalArr.length; j++) {
            finalChannelFutureServices[j] = channelFutureServices.get(finalArr[j]);
        }
        SERVICE_ROUTER_MAP.put(registryConfig.getServiceName(), finalChannelFutureServices);
    }

    private static Integer[] createWeightArr(List<ChannelFutureService> channelFutureServices) {
        List<Integer> weightArr = new ArrayList<>();
        for (int k = 0; k < channelFutureServices.size(); k++) {
            Integer weight = channelFutureServices.get(k).getWeight();
            int c = weight / 100;
            for (int i = 0; i < c; i++) {
                weightArr.add(k);
            }
        }
        Integer[] arr = new Integer[weightArr.size()];
        return weightArr.toArray(arr);
    }

    /**
     * 创建随机乱序数组
     *
     * @param arr
     * @return
     */
    private static Integer[] createRandomArr(Integer[] arr) {
        int total = arr.length;
        Random ra = new Random();
        for (int i = 0; i < total; i++) {
            int j = ra.nextInt(total);
            if (i == j) {
                continue;
            }
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
        return arr;
    }

    private int[] createRandomIndex(int len) {
        int[] arrInt = new int[len];
        Random ra = new Random();
        for (int i = 0; i < arrInt.length; i++) {
            arrInt[i] = -1;
        }
        int index = 0;
        while (index < arrInt.length) {
            int num = ra.nextInt(len);
            //如果数组中不包含这个元素则赋值给数组
            if (!contains(arrInt, num)) {
                arrInt[index++] = num;
            }
        }
        return arrInt;
    }


    public boolean contains(int[] arr, int key) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == key) {
                return true;
            }
        }
        return false;
    }


}
