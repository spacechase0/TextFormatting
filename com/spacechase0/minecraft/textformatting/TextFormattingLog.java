package com.spacechase0.minecraft.textformatting;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TextFormattingLog
{
	public static void info( String str )
	{
		logger.info( str );
	}
	
	public static void fine( String str )
	{
		logger.debug( str );
	}
	
	public static void severe( String str )
	{
		logger.error( str );
	}
	
	private static Logger makeLogger()
	{
		Logger logger = LogManager.getLogger( "SC0_TextFormatting" );
		
		return logger;
	}
	
	private static final Logger logger = makeLogger();
}
