package com.rbonestell.bcwidget.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BlockchainRequest
{

	private static String blockchainURL = "https://blockchain.info/latestblock";
	
	public static BlockchainResponse getBlockchainInfo()
	{
		BlockchainResponse resp = null;
		try
		{
			String rawResponse = WSClient.sendRequest(blockchainURL, WSClient.RequestType.GET);
			ObjectMapper mapper = new ObjectMapper();
			resp = (BlockchainResponse)mapper.readValue(rawResponse, BlockchainResponse.class);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return resp;
	}
	
}
