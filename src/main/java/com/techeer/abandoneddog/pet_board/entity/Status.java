package com.techeer.abandoneddog.pet_board.entity;

public enum Status {
	Awaiting_adoption, Adoption_complete;

	public static Status fromProcessState(String processState) {
		if ("보호중".equals(processState)) {
			return Awaiting_adoption;
		} else if ("개인보호중".equals(processState)) {
			return Awaiting_adoption;
		} else if ("공고중".equals(processState)) {
			return Awaiting_adoption;
		} else if ("종료(반환)".equals(processState)) {
			return Adoption_complete;
		} else if ("종료(안락사)".equals(processState)) {
			return Adoption_complete;
		} else if ("종료(기증)".equals(processState)) {
			return Adoption_complete;
		} else if ("종료(자연사)".equals(processState)) {
			return Adoption_complete;
		} else if ("종료(입양)".equals(processState)) {
			return Adoption_complete;
		} else if ("종료(방사)".equals(processState)) {
			return Adoption_complete;
		} else {
			return null;
		}
	}
}
