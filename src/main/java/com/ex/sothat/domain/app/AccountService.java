package com.ex.sothat.domain.app;


import com.ex.sothat.domain.dao.Account;
import com.ex.sothat.domain.dto.ReviewResponseDTO;
import com.ex.sothat.domain.dto.ReviewUpdateRequestDTO;
import com.ex.sothat.domain.dto.request.DuplicateNicknameRequestDto;
import com.ex.sothat.domain.dto.request.RequiredAccountInfoRequestDto;

import org.springframework.security.core.userdetails.UserDetails;
import java.util.List;

public interface AccountService {
    boolean AccountNicknameDuplicateCheck(DuplicateNicknameRequestDto duplicateNicknameRequestDto);
    Account loadUserStatus(Long AccountId, UserDetails userDetails);
    List<String> UnRequiredAccountInfoResponseDto();
    void AccountWithdraw(UserDetails userDetails);

    boolean patchAccountRequiredInfo(RequiredAccountInfoRequestDto requiredAccountInfoRequestDto);

    List<ReviewResponseDTO> getMyReviews(String username);

    void updateMyReview(Long reviewId, ReviewUpdateRequestDTO reviewUpdateRequestDTO, String username);

    void deleteMyReview(Long reviewId, String username);

    boolean isAccountNicknameDuplicated(String nickname);

    //newNewUser 탈퇴했거나 벤 먹은 유저 다시 가입할때 쓰임. 벤인지 탈퇴인지 구분 궈야 할듯.
    void recoverData(Account Account);
}
