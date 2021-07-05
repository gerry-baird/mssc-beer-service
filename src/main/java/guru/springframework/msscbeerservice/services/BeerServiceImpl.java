package guru.springframework.msscbeerservice.services;

import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.repository.BeerRepository;
import guru.springframework.msscbeerservice.web.controller.NotFoundException;
import guru.springframework.msscbeerservice.web.mappers.BeerMapper;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public BeerDto getById(UUID beerId) {

        BeerDto foundBeerDto = null;
        Beer foundBeer = null;

        Optional<Beer> beerOptional = beerRepository.findById(beerId);
        if(beerOptional.isPresent()){
            foundBeer = beerOptional.get();
            foundBeerDto = beerMapper.beerToBeerDto(foundBeer);
        }else{
            throw new NotFoundException();
        }

        return foundBeerDto;
    }

    @Override
    public BeerDto saveNewBeer(BeerDto beerDto) {

        //convert beerDto(web layer) into beer(domain layer)
        Beer beer = beerMapper.beerDtoToBeer(beerDto);

        //Save the new beer
        Beer savedBeer = beerRepository.save(beer);

        //Convert the saved beer into a dto for
        //return to the web layer
        BeerDto savedBeerDto = beerMapper.beerToBeerDto(savedBeer);

        return savedBeerDto;
    }

    @Override
    public BeerDto updateBeer(UUID beerId, BeerDto beerDto) {

        //Find the existing beer or throw an exception
        Beer beer = beerRepository.findById(beerId).orElseThrow(NotFoundException::new);

        //map the values from the dto to the beer
        beer.setBeerName(beerDto.getBeerName());
        beer.setBeerStyle(beerDto.getBeerStyle().name());
        beer.setPrice(beerDto.getPrice());
        beer.setUpc(beerDto.getUpc());

        return beerMapper.beerToBeerDto(beerRepository.save(beer));
    }
}
