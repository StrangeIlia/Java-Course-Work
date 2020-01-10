package bgty.vt_41.bi.web;

import bgty.vt_41.bi.entity.domain.Playlist;
import bgty.vt_41.bi.entity.domain.User;
import bgty.vt_41.bi.entity.domain.Video;
import bgty.vt_41.bi.entity.dto.ORReject;
import bgty.vt_41.bi.entity.dto.PlaylistsForm;
import bgty.vt_41.bi.entity.enums.EStatus;
import bgty.vt_41.bi.service.PlaylistsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("api/playlists")
public class PlaylistController {
    @Autowired
    PlaylistsService playlistsService;

    @GetMapping(path = {"/", "/index"})
    public Collection<Playlist> getPlaylists(@RequestParam String username) {
        return playlistsService.findByAuthor(username);
    }

    @GetMapping("/view")
    public Playlist getPlaylists(@RequestParam Integer id) {
        Optional<Playlist> optionalPlaylist = playlistsService.findById(id);
        return optionalPlaylist.orElse(null);
    }

    @PostMapping("/create")
    public Object createPlaylists(
            @RequestBody PlaylistsForm playlistsForm,
            Authentication authentication
    ) {
        User pseudoUser = (User) authentication.getPrincipal();
        Playlist createdPlaylist = playlistsService.create(pseudoUser, playlistsForm.getName());
        if (createdPlaylist == null)
            return new ORReject("Не удалось создать плейлист");
        else
            return new Object() {
                public String status = EStatus.SUCCESS.toString().toLowerCase();
                public Playlist playlist = createdPlaylist;
            };
    }

    @DeleteMapping("/delete")
    public void deletePlaylists(@RequestParam Integer id, Authentication authentication) {
        User pseudoUser = (User) authentication.getPrincipal();
        playlistsService.deleteById(pseudoUser, id);
    }

    @GetMapping("/videos")
    public Collection<Video> getVideos(@RequestParam Integer id) {
        return playlistsService.findVideosById(id);
    }

    @PostMapping("/add_video")
    public Object addVideoInPlaylist(@RequestParam Integer videoId,
                                     @RequestParam Integer playlistId,
                                     Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        playlistsService.addVideoInPlaylist(user, videoId, playlistId);
        return new Object() {
            public String status = EStatus.SUCCESS.toString().toLowerCase();
        };
    }

    @PostMapping("/delete_video")
    public void deleteVideoInPlaylist(@RequestParam Integer videoId,
                                      @RequestParam Integer playlistId,
                                      Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        playlistsService.deleteVideoInPlaylist(user, videoId, playlistId);
    }
}
