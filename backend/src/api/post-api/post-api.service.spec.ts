import { Test, TestingModule } from '@nestjs/testing';
import { PostApiService } from './post-api.service';
import { PrismaService } from '@/common/prisma/prisma.service';
import { PrismaModule } from '@/common/prisma/prisma.module';
import { PrismaProvider } from '@/common/prisma/prisma.provider';
import { mockDeep } from 'jest-mock-extended';
import { BlockFileService } from '@/domain/block-file/block-file.service';
import { BlockService } from '@/domain/block/block.service';
import { BucketService } from '@/common/bucket.service';
import { FileService } from '@/domain/file/file.service';
import { PostService } from '@/domain/post/post.service';

describe('PostApiService', () => {
  let service: PostApiService;
  let prisma: PrismaProvider;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      imports: [PrismaModule],
      providers: [
        PostApiService,
        PrismaService,
        PostService,
        BucketService,
        BlockService,
        BlockFileService,
        PrismaProvider,
        FileService,
      ],
    })
      .overrideProvider(PrismaService)
      .useValue(mockDeep<PrismaService>())
      .compile();

    service = module.get<PostApiService>(PostApiService);
    prisma = module.get(PrismaProvider);
  });

  describe('create', () => {
    it('게시글을 작성할 수 있다.', async () => {
      await expect(
        service.write({
          title: 'Test Post',
          blocks: [
            {
              type: 'text',
              content: 'This is Text Block.',
            },
            {
              type: 'media',
              content: 'This is Media Block.',
              latitude: 8.1414,
              longitude: -74.3538,
              files: [
                {
                  uuid: '589bb04b-9aa3-4a2b-bc66-8ea8235c4c01',
                },
              ],
            },
          ],
        }),
      ).resolves;
    });
  });
});