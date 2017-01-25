package com.metashift.aws

import com.amazonaws.resources.ec2.*
import com.amazonaws.resources.ec2.internal.*

/**
 * Created by mindonfire on 1/20/17.
 */

class AwsSession {

    /**
     * This is used to create an AWS Session from the cached file's data.
     * VPC, SG, Subnet, Instance
     *
     * @param element
     * @param key
     * @return
     */
    public static Map<String, Object> generateAwsSession(Map<String,Object> cacheFileData, EC2 ec2) {
        HashMap<String,Object> map = new HashMap<>()
        cacheFileData.each { it ->
            if(it.getValue() instanceof Map){
                Map ecObj = (Map)it.getValue()
                if(ecObj.clazz.equalsIgnoreCase(VpcImpl.class.getName())){
                    map.put(it.getKey(), ec2.getVpc((String)ecObj.id))
                }else if(ecObj.clazz.equalsIgnoreCase(SecurityGroupImpl.class.getName())){
                    map.put(it.getKey(), ec2.getSecurityGroup((String)ecObj.id))
                }else if(ecObj.clazz.equalsIgnoreCase(SubnetImpl.class.getName())){
                    map.put(it.getKey(), ec2.getSubnet((String)ecObj.id))
                }else if(ecObj.clazz.equalsIgnoreCase(InstanceImpl.class.getName())){
                    map.put(it.getKey(), ec2.getInstance((String)ecObj.id))
                }
            }else{
                map.put(it.getKey(), it.getValue())
            }
        }
        map
    }

}