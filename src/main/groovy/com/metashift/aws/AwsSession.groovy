package com.metashift.aws

import com.amazonaws.resources.ec2.*

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
                if(Vpc.getClass().getName().equalsIgnoreCase(it.getValue().clazz)){
                    map.put(it.getKey(), ec2.getVpc(it.getValue().id))
                }else if(SecurityGroup.getClass().getName().equalsIgnoreCase(it.getValue().clazz)){
                    map.put(it.getKey(), ec2.getSecurityGroup(it.getValue().id))
                }else if(Subnet.getClass().getName().equalsIgnoreCase(it.getValue().clazz)){
                    map.put(it.getKey(), ec2.getSubnet(it.getValue().id))
                }else if(Instance.getClass().getName().equalsIgnoreCase(it.getValue().clazz)){
                    map.put(it.getKey(), ec2.getInstance(it.getValue().id))
                }else{
                    out << "AWS Object cannot be created and AwsSession must be updated to support it -> ${it.getValue().clazz}"
                    out.flush()
                }
            }else{
                map.put(it.getKey(), it.getValue())
            }
        }
    }

}