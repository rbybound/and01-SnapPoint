import { ApiProperty } from '@nestjs/swagger';
import { File } from '@prisma/client';

export class FileDto {
  @ApiProperty({ description: '파일의 고유 식별자를 나타냅니다.' })
  uuid: string;

  @ApiProperty({ description: '파일에 접근 가능한 URL 주소입니다.' })
  url: string;

  @ApiProperty({ description: '720P 리사이징된 파일의 URL 주소입니다.' })
  url_720p: string | null;

  @ApiProperty({ description: '480P 리사이징된 파일의 URL 주소입니다.' })
  url_480p: string | null;

  @ApiProperty({ description: '144P 리사이징된 파일의 URL 주소입니다.' })
  url_144p: string | null;

  @ApiProperty({ description: '파일의 MIME 타입을 나타냅니다.' })
  mimeType: string;

  @ApiProperty({ description: '파일의 썸네일이 있는 경우 썸네일 파일의 고유 식별자입니다.' })
  thumbnailUuid: string | null;

  static of(file: File): FileDto {
    const { uuid, url, mimeType, isProcessed, thumbnailUuid } = file;
    if (!isProcessed) {
      return { uuid, url, mimeType, url_144p: null, url_480p: null, url_720p: null, thumbnailUuid: thumbnailUuid };
    }

    return {
      uuid,
      url,
      mimeType,
      url_144p: `${url}_144p`,
      url_480p: `${url}_480p`,
      url_720p: `${url}_720p`,
      thumbnailUuid: thumbnailUuid,
    };
  }
}
