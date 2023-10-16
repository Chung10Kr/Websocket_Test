package com.example.demo.visual.visualFlow;

import java.time.LocalTime;

public class Flow {
    private FlowType dataType; // Data Type( DATA, ERROR )
    private String requestId; // Request Id
    private String processId; //Process Id
    private String thread_name; // Thread Name
    private String callClass; // 호출한 클래스명
    private String label; // Label
    private LocalTime localTime; // 수집시간
    private Object value; // Data

    @Override
    public String toString() {
        return "Flow{" +
                "dataType=" + dataType +
                ", requestId='" + requestId + '\'' +
                ", processId='" + processId + '\'' +
                ", thread_name='" + thread_name + '\'' +
                ", callClass='" + callClass + '\'' +
                ", label='" + label + '\'' +
                ", localTime=" + localTime +
                ", value=" + value +
                '}';
    }

    public FlowType getDataType() {
        return dataType;
    }

    public void setDataType(FlowType dataType) {
        this.dataType = dataType;
    }

    public String getCallClass() {
        return callClass;
    }

    public void setCallClass(String callClass) {
        this.callClass = callClass;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public void setThread_name(String thread_name) {
        this.thread_name = thread_name;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setLocalTime(LocalTime localTime) {
        this.localTime = localTime;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getProcessId() {
        return processId;
    }

    public String getThread_name() {
        return thread_name;
    }

    public String getLabel() {
        return label;
    }

    public LocalTime getLocalTime() {
        return localTime;
    }

    public Object getValue() {
        return value;
    }
}
