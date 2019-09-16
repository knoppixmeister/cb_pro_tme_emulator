package com.cb.pro.tme.emu;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class WebSocketHandler extends TextWebSocketHandler {
	private List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

	private TMEEventsBroadcasterThread tmeEventsBroadcasterThread = new TMEEventsBroadcasterThread();

	public WebSocketHandler() {
		tmeEventsBroadcasterThread.start();
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessions.add(session);
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		sessions.remove(session);
	}

	class TMEEventsBroadcasterThread extends Thread {
		@Override
		public void run() {
			String eventData;
			while(true) {
				try {
					eventData = Application.TME_EVENTS_QUEUE.take();

					for(WebSocketSession s : sessions) {
						if(s != null && s.isOpen())	s.sendMessage(new TextMessage(eventData));
					}
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
