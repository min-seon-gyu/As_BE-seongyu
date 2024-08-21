package Auction_shop.auction.domain.address.service;

import Auction_shop.auction.domain.address.Address;
import Auction_shop.auction.domain.member.Member;
import Auction_shop.auction.domain.address.repository.AddressRepository;
import Auction_shop.auction.domain.member.repository.MemberRepository;
import Auction_shop.auction.web.dto.address.AddressRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AddressService {

    private final MemberRepository memberRepository;
    private final AddressRepository addressRepository;

    //주소 추가
    @Transactional
    public void addAddress(Long memberId, AddressRequestDto addressRequestDto){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException(memberId + "에 해당하는 회원이 없습니다."));

        Address address = Address.builder()
                .name(addressRequestDto.getName())
                .phoneNumber(addressRequestDto.getPhoneNumber())
                .address(addressRequestDto.getAddress())
                .detailAddress(addressRequestDto.getDetailAddress())
                .zipcode(addressRequestDto.getZipcode())
                .defaultAddress(false)
                .build();

        addressRepository.save(address);

        member.addAddress(address);
    }

    //주소 삭제
    @Transactional
    public void deleteAddress(Long memberId, Long addressId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException(memberId + "에 해당하는 회원이 없습니다."));

        Optional<Address> addressToRemove = member.getAddresses().stream()
                .filter(address -> address.getId().equals(addressId))
                .findFirst();

        if (addressToRemove.isPresent()) {
            Address address = addressToRemove.get();
            if (address.isDefaultAddress()) {
                System.out.println("기본 주소지로 설정된 주소는 삭제가 불가능합니다.");
                throw new IllegalArgumentException("기본 주소지로 설정된 주소는 삭제가 불가능합니다.");
            }

            member.getAddresses().remove(address);
        }
    }

    //주소 수정
    @Transactional
    public void updateAddress(Long memberId, Long addressId,AddressRequestDto addressRequestDto){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException(memberId + "에 해당하는 회원이 없습니다."));

        Optional<Address> addressToUpdate = member.getAddresses().stream()
                .filter(address -> address.getId().equals(addressId))
                .findFirst();

        addressToUpdate.ifPresent(address ->{
            address.updateAddress(
                    addressRequestDto.getPhoneNumber(),
                    addressRequestDto.getName(),
                    addressRequestDto.getAddress(),
                    addressRequestDto.getDetailAddress(),
                    addressRequestDto.getZipcode());
        });
    }

    //기본 주소지 설정
    @Transactional
    public void updateDefaultAddress(Long memberId, Long addressId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException(memberId + "에 해당하는 회원이 없습니다."));

        member.getAddresses().forEach(address -> {
            if (address.isDefaultAddress()) {
                address.setDefaultAddress(false);
            }
        });

        Optional<Address> addressToUpdate = member.getAddresses().stream()
                .filter(address -> address.getId().equals(addressId))
                .findFirst();

        addressToUpdate.ifPresent(address -> {
            address.setDefaultAddress(true);
        });
    }
}
