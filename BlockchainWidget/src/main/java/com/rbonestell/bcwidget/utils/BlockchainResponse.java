package com.rbonestell.bcwidget.utils;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class BlockchainResponse implements Serializable
{
	private static final long serialVersionUID = -5196363532275776169L;
	public int time;
	public int height;
}
